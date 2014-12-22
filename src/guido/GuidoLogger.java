package guido;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuidoLogger<T> {
	private Logger logger;
	
	public GuidoLogger(Class<T> clazz, String fileName) throws SecurityException, IOException {
		if (fileName == null || fileName.trim().length() == 0) {
			throw new RuntimeException("Must provide the log filename as second parameter.");
		}
		
		logger = Logger.getLogger(clazz.getName());
		System.out.println("Adding a console handler.");
		logger.addHandler(new ConsoleHandler());
		System.out.println("Adding a file hanlder to file "+ fileName);
		logger.addHandler(new FileHandler(fileName));
	}

	public void i(String message) {
		logger.log(Level.INFO, message);
	}
	
	public void e(String message) {
		logger.log(Level.SEVERE, message);
	}
	
	public void e(Exception ex) {
		ex.printStackTrace();
		logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
	}
	
}
