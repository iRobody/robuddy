package com.irobuddy.webcontrol.utils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.irobuddy.webcontrol.R;

import android.content.Context;


public class Utils {
	private static String TAG = "irobuddy_webcontrol";
	
	
	public static void clearResource(String targetDir) {
        String[] str ={"rm", "-r", targetDir};

        try { 
            Process ps = Runtime.getRuntime().exec(str);
            try {
                ps.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void buildResource(Context context, String targetDir) {
        String[] str ={"mkdir", targetDir};

        try { 
            Process ps = Runtime.getRuntime().exec(str);
            try {
                ps.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        
            copyResourceFile(context, R.raw.index, targetDir + "/index.html"  );
            copyResourceFile(context, R.raw.websocket, targetDir + "/websocket.js"  );
            copyResourceFile(context, R.raw.style, targetDir + "/style.css"  );
            
            copyResourceFile(context, R.raw.upbutton, targetDir + "/upbutton.jpg"  );
            copyResourceFile(context, R.raw.downbutton, targetDir + "/downbutton.jpg"  );
            copyResourceFile(context, R.raw.leftbutton, targetDir + "/leftbutton.jpg"  );
            copyResourceFile(context, R.raw.rightbutton, targetDir + "/rightbutton.jpg"  );
         
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private static void copyResourceFile(Context context, int rid, String targetFile) throws IOException {
		InputStream fin = context.getResources().openRawResource(rid);
	    FileOutputStream fos = new FileOutputStream(targetFile);  

	    int     length;
	    byte[] buffer = new byte[1024*32]; 
	    while( (length = fin.read(buffer)) != -1){
	    	fos.write(buffer,0,length);
	    }
	    fin.close();
	    fos.close();
	}
	/*
	
	//Copy all of HTTP Resources files from assets in APK to SDK dir.
	 
	public static void CopyAssets(Context context, String folderName) {
		
		File folder = new File(folderName);
		boolean success = false;
		if (folder.exists()) {
			//clean up all files under it.
			deleteDirectory(folder);
		}
		success = folder.mkdir();
		
		//doCopyAssets(context, folderName+"/");
		
		if (success) {
		    // Do something on success
			Logger.getDefault().debug(TAG, "SDK is ready.");
			doCopyAssets(context, folderName+"/");
		} else {
		    // Do something else on failure 
			Logger.getDefault().debug(TAG, "Create DIR failed, pls check whether SDK is available. ");
		}
		
	}
	
	private static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	}
	
	
	private static void doCopyAssets(Context context, String targetPath) {
			AssetManager assetManager = context.getAssets();
		    String[] files = null;
		    try {
		        files = assetManager.list("web");
		    } catch (IOException e) {
		        Logger.getDefault().debug(TAG, e.getMessage());
		    }
		    for(String filename : files) {
		        InputStream in = null;
		        OutputStream out = null;
		        try {
		          Logger.getDefault().debug(TAG, "COPY file " + filename);
		          in = assetManager.open("web/"+filename);
		          out = new FileOutputStream(targetPath + filename);
		          copyFile(in, out);
		          in.close();
		          in = null;
		          out.flush();
		          out.close();
		          out = null;
		        } catch(Exception e) {
		        	Logger.getDefault().debug(TAG, e.getMessage());
		        }       
		    }
		}
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	*/
	
}
