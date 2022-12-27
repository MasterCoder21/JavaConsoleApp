package wtf.godlydev.console.utils;

import java.io.InputStream;
import java.io.OutputStream;

public class EmptyProcess extends Process {
	
	public EmptyProcess() {}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public InputStream getErrorStream() {
		return null;
	}

	@Override
	public int waitFor() throws InterruptedException {
		return 0;
	}

	@Override
	public int exitValue() {
		return 0;
	}

	@Override
	public void destroy() {
		
	}

}
