package net.javaguides.springboot.helloworldapp.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javafx.util.Pair;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.helloworldapp.bean.Greeting;

import javax.imageio.ImageIO;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class GreetingController {

	private static final String template = "Hello, %s!";
	private static final String salt = "MT1xq2vyW4pt5M3k";
	private final AtomicLong counter = new AtomicLong();

	public static String getSalt() {
		return salt;
	}

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	private static String generateCaptchaTextMethod1() {

		Random rdm=new Random();
		int rl=rdm.nextInt(); // Random numbers are generated.
		String hash1 = Integer.toHexString(rl); // Random numbers are converted to Hexa Decimal.

		return hash1;
	}

	public static byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, 10000, 64);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}


	@RequestMapping("/getimage")
	public Pair<byte[], String> getImage() {

		String captchaStr="";
		byte[] encoded;
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		captchaStr = generateCaptchaTextMethod1();

		encoded = hash(captchaStr.toCharArray(), salt.getBytes());

		try {

			int width=100;      int height=40;

			Color bg = new Color(0,255,255);
			Color fg = new Color(0,100,0);

			Font font = new Font("Arial", Font.BOLD, 20);
			BufferedImage cpimg =new BufferedImage(width,height,BufferedImage.OPAQUE);
			Graphics g = cpimg.createGraphics();

			g.setFont(font);
			g.setColor(bg);
			g.fillRect(0, 0, width, height);
			g.setColor(fg);
			g.drawString(captchaStr,10,25);

			ImageIO.write(cpimg, "jpeg", outputStream);
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Pair<>(encoded, Base64.getEncoder().encodeToString(outputStream.toByteArray()));
	}
}