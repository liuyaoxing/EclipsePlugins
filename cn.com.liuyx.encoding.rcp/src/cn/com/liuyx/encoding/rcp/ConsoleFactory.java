package cn.com.liuyx.encoding.rcp;

import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleFactory implements IConsoleFactory {

	private static MessageConsole console = new MessageConsole("样式显示窗口", null);

	/** */
	/**
	 * 描述:打开控制台
	 * */
	public void openConsole() {
		showConsole();
	}

	/** */
	/**
	 * 描述:显示控制台
	 * */
	public static void showConsole() {
		try {
			if (console != null) {
				IConsoleManager manager = ConsolePlugin.getDefault()
						.getConsoleManager();
				IConsole[] existing = manager.getConsoles();
				boolean exists = false;
				for (int i = 0; i < existing.length; i++) {
					if (console == existing[i])
						exists = true;
				}
				if (!exists) {
					manager.addConsoles(new IConsole[] { console });
				}
				manager.showConsoleView(console);
				MessageConsoleStream stream = console.newMessageStream();
				stream.write("测试!");
				System.setOut(new PrintStream(stream));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * 描述:关闭控制台
	 * */
	public static void closeConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault()
				.getConsoleManager();
		if (console != null) {
			manager.removeConsoles(new IConsole[] { console });
		}
	}

	public static MessageConsole getConsole() {
		return console;
	}
}
