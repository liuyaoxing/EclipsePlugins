package cn.com.liuyx.encoding.jobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;

import cn.com.liuyx.encoding.Activator;

/**
 * 设置编码。
 * 
 * @author 刘尧兴(liuyaoxing@gmail.com)
 */
public class SetEncodingJob extends Job {

	private IStructuredSelection selection;
	private String newEncoding;

	class ChangeEncodingVisitor implements IResourceVisitor {
		private IProgressMonitor monitor;

		public ChangeEncodingVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		public boolean visit(IResource resource) throws CoreException {
			if (monitor.isCanceled())
				return false;
			return setEncoding(monitor, resource);
		}

	}

	public SetEncodingJob(IStructuredSelection selection, String newEncoding) {
		super("Setting file encoding:" + newEncoding);
		this.selection = selection;
		this.newEncoding = newEncoding;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Setting file encoding", IProgressMonitor.UNKNOWN);
		IResource[] resources = new IResource[selection.size()];
		try {
			System.arraycopy(selection.toArray(), 0, resources, 0, selection.size());
			new ResourceTraversal(resources, IResource.DEPTH_INFINITE, 0).accept(new ChangeEncodingVisitor(monitor));
		} catch (CoreException e) {
			Activator.logException(e);
		}
		// 刷新
		for (IResource s : resources) {
			try {
				s.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException e) {
				Activator.logException(e);
			}
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	private boolean setEncoding(final IProgressMonitor monitor, IResource res) {
		if (res instanceof IFile) {
			IFile file = (IFile) res;
			monitor.subTask("Process file " + file.getName());
			String suffix = file.getFileExtension();
			if (suffix != null && !file.isLinked() && !file.isPhantom()) {
				// 文件内容需要转码，类似一个拷贝/设定/粘贴的过程
				if (!suffix.equalsIgnoreCase("java")
						&& !suffix.equalsIgnoreCase("txt") //
						&& !suffix.equalsIgnoreCase("ini") && !suffix.equalsIgnoreCase("dsr")
						&& !suffix.equalsIgnoreCase("json") && !suffix.equalsIgnoreCase("lic")//
						&& !suffix.equalsIgnoreCase("project") && !suffix.equalsIgnoreCase("abf")//
						&& !suffix.equalsIgnoreCase("hlp") && !suffix.equalsIgnoreCase("xml")//
						&& !suffix.equalsIgnoreCase("dat") && !suffix.equalsIgnoreCase("fmt")//
						&& !suffix.equalsIgnoreCase("abd") && !suffix.equalsIgnoreCase("comment")//
						&& !suffix.equalsIgnoreCase("info") && !suffix.equalsIgnoreCase("sql")//
						&& !suffix.equalsIgnoreCase("fc") && !suffix.equalsIgnoreCase("py")//
						&& !suffix.equalsIgnoreCase("src") && !suffix.equalsIgnoreCase("pkg")//
						&& !suffix.equalsIgnoreCase("properties") && !suffix.equalsIgnoreCase("dict")//
						 && !suffix.equalsIgnoreCase("pck")
						)
					return false;
				try {
					String encoding = file.getCharset();
					if (newEncoding == null || newEncoding.equalsIgnoreCase(encoding)) {
						return false;
					}
					if (!file.isReadOnly()) {
						InputStream inputstream = file.getContents();
						IFileStore store = EFS.getStore(file.getLocationURI());
						int file_size = (int) store.fetchInfo().getLength();
						byte[] buffer = new byte[file_size];
						inputstream.read(buffer);
						// 文件的内容
						String orignal = new String(buffer, encoding);
						if (suffix.equalsIgnoreCase("abf") || suffix.equalsIgnoreCase("xml")
								|| suffix.equalsIgnoreCase("info") || suffix.equalsIgnoreCase("fc")
								|| suffix.equalsIgnoreCase("src") || suffix.equalsIgnoreCase("pkg")
								|| suffix.equalsIgnoreCase("dict")|| suffix.equalsIgnoreCase("pck")) {
							String oldEndoding1 = String.format("encoding=\"%s\"", encoding.toLowerCase());
							String oldEndoding2 = String.format("encoding=\"%s\"", encoding.toUpperCase());
							String newEndoding = String.format("encoding=\"%s\"", newEncoding);
							orignal = orignal.replace(oldEndoding1, newEndoding);
							orignal = orignal.replace(oldEndoding2, newEndoding);
						}
						if(suffix.equalsIgnoreCase("py")) {
							String oldEndoding1 = String.format("# -*- coding: %s -*-", encoding.toLowerCase());
							String oldEndoding2 = String.format("# -*- coding: %s -*-", encoding.toUpperCase());
							String newEndoding = String.format("# -*- coding: %s -*-", newEncoding);
							orignal = orignal.replace(oldEndoding1, newEndoding);
							orignal = orignal.replace(oldEndoding2, newEndoding);
						}
						// 按新编码转换后的内容
						ByteArrayInputStream byte_input = new ByteArrayInputStream(orignal.getBytes(newEncoding));
						// 写入Eclipse文件
						file.setContents(byte_input, IFile.FORCE, monitor);
					}
					// 设置新编码
					file.setCharset(newEncoding, monitor);

					System.out.println(String.format("转换完成! 原编码:%s, 新编码:%s, 文件:%s", encoding, file.getCharset(), file));
				} catch (CoreException e) {
					Activator.logException(e);
				} catch (IOException e) {
					Activator.logException(e);
				}
			}
			return false;
		} else {
			return true;
		}
	}

}
