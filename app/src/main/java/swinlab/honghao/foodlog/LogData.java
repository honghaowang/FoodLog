package swinlab.honghao.foodlog;

import android.os.Environment;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by kinse on 3/17/2016.
 */
public class LogData {
    public String foodInfo, timeStamp, check;
    public static String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "SwinLab";
    public static String filename = filePath + File.separator + "FoodLog.txt";

    public LogData(String food, String time, String check){
        foodInfo = food;
        timeStamp = time;
        this.check = check;
    }

    public static ArrayList<LogData> readFromFile() throws Exception{
        byte Buffer[] = new byte[1024];
        ArrayList<LogData> result = new ArrayList<LogData>();
        String []data = null;
        FileInputStream fin = null;
        ByteArrayOutputStream outputStream = null;
        File file = new File(filename);
        if(!file.exists()){
            Log.e("FileWrite", "File creation is failed");
        }
        fin = new FileInputStream(file);
        for(int i=0;i<3; i++) {
                int len = fin.read(Buffer);
                outputStream = new ByteArrayOutputStream();
                outputStream.write(Buffer, 0, len);
                data[i] = new String(outputStream.toByteArray());
        }
        result.add(new LogData(data[0], data[1], data[2]));
        return result;
    }

    public static void writeToFile(LogData data) {
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();

        file = new File(filename);
        if(!file.exists()){
            Log.e("FileWrite", "File creation is failed");
        }

        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file, true);
            String msg = data.foodInfo + "\n" + data.timeStamp + "\n" + data.check + "\n";
            outputStream.write(msg.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
