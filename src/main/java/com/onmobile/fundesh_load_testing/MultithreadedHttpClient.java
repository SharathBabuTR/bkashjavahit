package com.onmobile.fundesh_load_testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;


public class MultithreadedHttpClient extends Thread{
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private int hitsPerThread, sleepIntervalInMilliSeconds;
	private String outputFilesLocation;
	
	public MultithreadedHttpClient(int hitsPerThread, int sleepIntervalInMilliSeconds, String outputFilesLocation) {
		this.hitsPerThread = hitsPerThread;
		this.sleepIntervalInMilliSeconds = sleepIntervalInMilliSeconds;
		this.outputFilesLocation = outputFilesLocation;
	}

	public void run() {
		try{
			//System.out.println("outputFilesLocation = "+this.outputFilesLocation);
			File outputDir = new File(this.outputFilesLocation);
			if(!outputDir.exists())
				outputDir.mkdirs();
			//this.currentThreadOutputFile = new File(outputFilesLocation+File.pathSeparator+Thread.currentThread().getName()+".txt");
			//System.out.println("filePath = "+this.currentThreadOutputFile.toPath());
			//this.currentThreadOutputFile.createNewFile();
		}catch (Exception e) {
			e.printStackTrace();
		}
    		for(int i=0; i<this.hitsPerThread;i++) {
    			try {
					sendGET();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			try {
					Thread.sleep(this.sleepIntervalInMilliSeconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
	}
	
	private void sendGET() throws IOException {
		URL obj = new URL("https://fundeshstg.onmohub.com/api/payment/subscription/create?mode=gdn&prismKey=CONTEST_79TK_1M&payerReference=1745668428&country=BANGLADESH&serviceId=2028&lang=BEN&doPayment=true");
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        SSLContext.setDefault(ctx);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		//logger.debug("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			Date currentTime = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			String outputLineToBeWritten = sdf.format(currentTime)+" "+response.toString()+System.lineSeparator();
			Path path = Paths.get(this.outputFilesLocation+File.separator+Thread.currentThread().getName()+".txt");
			Files.write(path, outputLineToBeWritten.getBytes(StandardCharsets.UTF_8),StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			//logger.debug(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}
    private static class DefaultTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws java.security.cert.CertificateException {
			// TODO Auto-generated method stub
			
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws java.security.cert.CertificateException {
			// TODO Auto-generated method stub
			
		}
    }	

}
