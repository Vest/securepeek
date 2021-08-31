package com.test.securepeek.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

public class Peek extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		String nodes = printnode(0, root);
		MessageDialog.openInformation(window.getShell(), "Content of SecureStorage", nodes);

		return null;
	}

	private String printnode(int i, ISecurePreferences node)  {
		StringBuffer ibuffer = new StringBuffer();
		for (int n = 0; n < i; n++) {
			ibuffer.append("  ");
		}
		String indent = ibuffer.toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append(printkeys(i, node));
		String children[] = node.childrenNames();
		for (int n = 0; n < children.length; n++) {
			buffer.append(indent).append("node: ").append(children[n]).append(":\n");
			buffer.append(printnode(i + 1, node.node(children[n])));
		}
		System.out.println(buffer.toString());
		return buffer.toString();

	}

	private String printkeys(int i, ISecurePreferences node) {
		StringBuffer indent = new StringBuffer();
		for (int n = 0; n < i; n++) {
			indent.append("  ");
		}		
		StringBuffer buffer = new StringBuffer();

		String keys[] = node.keys();
		
		for (int n = 0; n < keys.length; n++) {
			
			try {
				buffer.append(indent).append(keys[n]).append(":");
				buffer.append(node.get(keys[n], "N/A"));
			} catch (StorageException e) {
				e.printStackTrace();
				buffer.append("N/A - cannot find provider");
			} finally {
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

}
