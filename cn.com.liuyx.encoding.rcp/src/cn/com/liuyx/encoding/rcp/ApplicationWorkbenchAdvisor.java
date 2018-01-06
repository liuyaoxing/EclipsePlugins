package cn.com.liuyx.encoding.rcp;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.osgi.framework.Bundle;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	@SuppressWarnings("restriction")
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		configurer.setSaveAndRestore(true);

		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
				false);

		IDE.registerAdapters();

		final String ICONS_PATH = "icons/full/";

		Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);

		declareWorkbenchImage(configurer, ideBundle,
				IDE.SharedImages.IMG_OBJ_PROJECT, ICONS_PATH
						+ "obj16/prj_obj.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, ICONS_PATH
						+ "obj16/cprj_obj.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW, ICONS_PATH
						+ "eview16/problems_view.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_ERROR,
				ICONS_PATH + "eview16/problems_view_error.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_WARNING,
				ICONS_PATH + "eview16/problems_view_warning.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDEInternalWorkbenchImages.IMG_OBJS_ERROR_PATH, ICONS_PATH
						+ "obj16/error_tsk.gif", true);

		declareWorkbenchImage(configurer, ideBundle,
				IDEInternalWorkbenchImages.IMG_OBJS_WARNING_PATH, ICONS_PATH
						+ "obj16/warn_tsk.gif", true);
	}

	private void declareWorkbenchImage(IWorkbenchConfigurer configurer_p,
			Bundle ideBundle, String symbolicName, String path, boolean shared) {
		URL url = ideBundle.getEntry(path);
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		configurer_p.declareImage(symbolicName, desc, shared);
	}
}
