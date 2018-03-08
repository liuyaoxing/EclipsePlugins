package cn.com.liuyx.encoding.rcp;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "cn.com.liuyx.encoding.rcp.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		{
			IFolderLayout folderLayout = layout.createFolder("folder", IPageLayout.LEFT, 0.25f, editorArea);
			folderLayout.addView("org.eclipse.ui.views.ResourceNavigator");
		}
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.BOTTOM, 0.5f, editorArea);
		folder.addView("org.eclipse.ui.console.ConsoleView");
		
	}
}
