import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * ASSUME BREACH ACTIVITY
 * 
 * THe idea here is to think about the requirements given. We must store passwords. But we
 * consider what if the the file storing passwords is leaked, has incorrect permission, etc.
 * We assume the file is already compromised, so we take action based on that assumption.
 * 
 * If we want to keep a secret a secret in plain view, we can store a hash result instead 
 * of the password.
 * 
 * we will return to this code at a later time when discussing hashes specifically.
 */
public class Main {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		//Prompt the user
		System.out.println("Welcome to the Sign Up. Enter an email and password to signup.");
		System.out.print("> ");
		
		Scanner keyboard = new Scanner(System.in);
		
		//Gather input from user
		String username = keyboard.nextLine();
		String password = "";
		
		//Check if user is in whitelist
		if(inWhitelist(username)) {
			System.out.print("Valid username. Enter a password\n>");
			password = keyboard.nextLine();
		}else{
			System.out.println("You're no allowed to signup. Contact your immediate supervisor.");
			System.exit(1);
		}
		
		//hash password
		String hash = md5hash(password);
		
		//Insert input into file
		insert(username, hash);
		System.out.println("Successfully added to list.");
		
		//Destroy keyboard
		keyboard.close();
	}
	
	/*
	 * HASH THE PASSWORD TO ELIMINATE THE NEED TO STORE PASSWORDS IN PLAIN TEXT
	 */
	public static String md5hash(String password) throws NoSuchAlgorithmException {
	    MessageDigest md5 = MessageDigest.getInstance("MD5");
	    
	    md5.update(password.getBytes());
	    
	    byte[] digest = md5.digest();
	    
	    StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

	    return sb.toString(); 
	}

	private static void insert(String username, String password) throws IOException {
		//check if file exists.
		File f = new File("user.db");
		FileWriter fw = new FileWriter(f, true);
		
		//write to the file
		fw.append(username + ":" + password + "\n");
		
		fw.close();
	}

	private static boolean inWhitelist(String username) throws FileNotFoundException {
		File file = new File("white.list");
		Scanner whitelist = new Scanner(file);
		
		//Burn first line which is a header.
		whitelist.nextLine();
		
		while(whitelist.hasNext()) {
			String[] fields = whitelist.nextLine().split(":");
			
			int id = Integer.parseInt(fields[0]);
			String whitelist_username = fields[1].trim();
			
			if(whitelist_username.equals(username)) {
				whitelist.close();
				return true;
			}
		}
		
		whitelist.close();

		return false;
	}

}
