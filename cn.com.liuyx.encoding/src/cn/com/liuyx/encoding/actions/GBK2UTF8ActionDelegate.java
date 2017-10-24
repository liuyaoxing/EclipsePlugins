/**
 * 
 */
package cn.com.liuyx.encoding.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import cn.com.liuyx.encoding.jobs.SetEncodingJob;

/**
 * GBK转编码。
 * 
 * @author 刘尧兴(liuyaoxing@gmail.com)
 */
public class GBK2UTF8ActionDelegate implements IObjectActionDelegate {

	protected IWorkbenchPart part;
	protected IStructuredSelection selection;

	@Override
	public void run(IAction action) {
		SetEncodingJob job = new SetEncodingJob(this.selection, "utf-8");
		job.schedule();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
	}

}
