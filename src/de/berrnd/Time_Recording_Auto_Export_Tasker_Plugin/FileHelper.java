package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileHelper {
    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream inStream = new FileInputStream(sourceFile);
        FileOutputStream outStream = new FileOutputStream(destinationFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
