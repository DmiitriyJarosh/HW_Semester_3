package sample;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Receiver implements Runnable {
    private Socket socket;
    private volatile boolean started;
    private Client client;
    private volatile boolean askToStart;
    private String selectedFilter;
    private volatile boolean askToBreak;
    private BufferedImage image;
    private ProgressUpdater progressUpdater;
    private static final int serverPort = 10001;
    private static final String serverIP = "127.0.0.1";
    private DataInputStream in;
    private volatile boolean active;
    private Lock IOlock;
    private DataOutputStream out;

    public Receiver(Client client) {
        active = true;
        askToStart = false;
        askToBreak = false;
        IOlock = new ReentrantLock();
        started = false;
        progressUpdater = null;
        this.client = client;
        try {
            InetAddress ipAddress = InetAddress.getByName(serverIP);
            socket = new Socket(ipAddress, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            client.setFilterList(getFiltersList());
        } catch (Exception e) {
            System.out.println("Server is not available!!");
            System.exit(0);
        }
    }

    public void setAskToBreak(boolean askToBreak) {
        this.askToBreak = askToBreak;
    }

    public void setAskToStart(boolean askToStart) {
        this.askToStart = askToStart;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setSelectedFilter(String selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public List<String> getFiltersList() {
        LinkedList<String> list = new LinkedList<>();
        try {
            int size = Integer.parseInt(in.readUTF());
            sendMSG("Ready");
            String[] filters = in.readUTF().split("\\|");
            for (int i = 0; i < size; i++) {
                list.add(filters[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void setProgressUpdater(ProgressUpdater progressUpdater) {
        this.progressUpdater = progressUpdater;
    }

    public void disconnect() {
        started = false;
        try {
            IOlock.lock();
            sendMSG("DISCONNECT");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOlock.unlock();
        }
        active = false;
    }

    public void sendStartMSG(String selectedFilter, BufferedImage image) {
        try {
            if (selectedFilter != null && !selectedFilter.equals("")) {
                sendMSG("START");
                started = true;
                System.out.println("started");
                in.readUTF();
                sendMSG(selectedFilter);
                in.readUTF();
                sendImage(image);
            } else {
                System.out.println("No filter have been chosen!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMSG(String MSG) throws IOException {
        out.writeUTF(MSG);
        out.flush();
    }

    private void sendImage(BufferedImage image) {
        try {
            System.out.println("send start");
            int height = image.getHeight();
            int width = image.getWidth();
            sendMSG(Integer.toString(image.getWidth()));
            in.readUTF();
            sendMSG(Integer.toString(image.getHeight()));
            in.readUTF();
            sendMSG(Integer.toString(image.getType()));
            in.readUTF();
            String s;
            for (int i = 0; i < width; i++) {
                s = "";
                for (int j = 0; j < height; j++) {
                    if (!askToBreak) {
                        s += Integer.toString(image.getRGB(i, j)) + "|";
                        //sendMSG(Integer.toString(image.getRGB(i, j)));
                        //in.readUTF();
                        progressUpdater.setProgress((i * height + j) / (double) (height * width) / 3.0);
                    } else {
                        progressUpdater.setProgress(0);
                        sendMSG("BREAK");
                        started = false;
                        System.out.println("send break");
                        askToBreak = false;
                        return;
                    }
                }
                sendMSG(s);
                in.readUTF();
            }
            System.out.println("send finish");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage receiveImage() {
        BufferedImage image = null;
        try {
            int width = Integer.parseInt(in.readUTF());
            sendMSG("Ready!");
            int height = Integer.parseInt(in.readUTF());
            sendMSG("Ready!");
            int type = Integer.parseInt(in.readUTF());
            sendMSG("Ready!");
            image = new BufferedImage(width, height, type);
            System.out.println("receive start");
            for (int i = 0; i < width; i++) {
                String[] pixels = in.readUTF().split("\\|");
                for (int j = 0; j < height; j++) {
                    if (!askToBreak) {
                        image.setRGB(i, j, Integer.parseInt(pixels[j]));
                        //image.setRGB(i, j, Integer.parseInt(in.readUTF()));
                        //sendMSG("Ready!");
                        progressUpdater.setProgress((i * height + j) / (double) (height * width) / 3.0 + 2.0 / 3);
                    } else {
                        in.readUTF();
                        progressUpdater.setProgress(0);
                        sendMSG("BREAK");
                        started = false;
                        System.out.println("receive break");
                        askToBreak = false;
                        return null;
                    }
                }
                sendMSG("Ready");
            }
            System.out.println("receive finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void finish() {
        progressUpdater.setProgress(1);
        started = false;
        client.getController().setStarted(false);
        client.getController().getButtonBreak().setDisable(true);
        client.getController().getButtonStart().setDisable(false);
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public void run() {
        String line;

        while (active) {
                try {
                    IOlock.lock();
                    if (askToBreak) {
                        progressUpdater.setProgress(0);
                        sendMSG("BREAK");
                        started = false;
                        askToBreak = false;
                    }
                    if (askToStart) {
                        sendStartMSG(selectedFilter, image);
                        askToStart = false;
                    }
                    if (started) {
                        line = in.readUTF();
                        switch (line) {
                            case "PROGRESS":
                                sendMSG("Ready!");
                                progressUpdater.setProgress(Double.parseDouble(in.readUTF()) / 3.0 + (1.0 / 3));
                                sendMSG("Ready!");
                                break;
                            case "FINISHED":
                                sendMSG("Ready!");
                                BufferedImage image = receiveImage();
                                if (image == null) {
                                    continue;
                                }
                                client.setBufferedImage(image);

                                //ImageIO.write(client.getBufferedImage(), "jpg", new File("test2.jpg"));
                                client.showImage();
                                finish();
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOlock.unlock();
                }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
