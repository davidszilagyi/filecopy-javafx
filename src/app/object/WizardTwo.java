package app.object;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by David Szilagyi on 2017. 06. 06..
 */
public abstract class WizardTwo implements Runnable {
    private File from;
    private File to;
    private long totalBytes;
    private long currentBytes;
    public boolean stopped;

    public WizardTwo(File src, File dest) {
        this.from = src;
        this.to = dest;
    }

    @Override
    public void run() {
        getTotalSize(from);
        try {
            copyProcess(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getTotalSize(File file) {
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String f : files) {
                getTotalSize(new File(file, f));
            }
        } else {
            totalBytes += file.length();
        }
    }

    public void deleteFile(File file) throws IOException {
        if(file.isDirectory()) {
            String[] files = file.list();
            for(String f: files) {
                deleteFile(new File(file, f));
            }
        } else {
            Files.deleteIfExists(file.toPath());
        }
    }

    private void copyProcess(File src, File dest) throws IOException, InterruptedException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String[] files = src.list();
            for (String file : files) {
                copyProcess(new File(src, file), new File(dest, file));
            }
        } else {
            boolean running = true;
            InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0 && running) {
                if (Thread.currentThread().isInterrupted()) {
                    running = false;
                }
                if(stopped) {
                    synchronized (this) {
                        wait();
                    }
                }
                currentBytes += length;
                updatePB(((double) currentBytes / totalBytes));
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            if (!running) {
                deleteFile(to);
                Files.deleteIfExists(to.toPath());
            }
        }
    }

    public abstract void updatePB(double current);
}
