package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Example implements net.Network_iface {

    // set the speed of the serial port
    public static int speed = 9600;
    private static Network network;

    private static boolean resend_active = false;

    private static CountDownLatch LATCH = new CountDownLatch(1);

    public static void main(String[] args) {


        network = new Network(0, new Example(), 255);

        // initializing reader from command line
        int i, inp_num = 0;
        String input;
        BufferedReader in_stream = new BufferedReader(new InputStreamReader(System.in));

        // getting a list of the available serial ports
        Vector<String> ports = network.getPortList();
        if (ports.size() == 0) {
            System.out.println("sorry, no serial ports were found on your computer\n");
            System.exit(0);
        }

        System.out.println("the following serial ports have been detected:");
        for (i = 0; i < ports.size(); ++i) {
            System.out.println("    " + Integer.toString(i + 1) + ":  " + ports.elementAt(i));
        }

        boolean valid_answer = false;
        if ( ports.size()==1 ){
            inp_num = 1;
        } else {
            while (!valid_answer) {
                System.out.println("enter the id (1,2,...) of the connection to connect to: ");
                try {
                    input = in_stream.readLine();
                    inp_num = Integer.parseInt(input);
                    if ((inp_num < 1) || (inp_num >= ports.size() + 1))
                        System.out.println("your input is not valid");
                    else
                        valid_answer = true;
                } catch (NumberFormatException ex) {
                    System.out.println("please enter a correct number");
                } catch (IOException e) {
                    System.out.println("there was an input error\n");
                    System.exit(1);
                }
            }
        }


        // connecting to the selected port
        if (network.connect(ports.elementAt(inp_num - 1), speed)) {
            System.out.println();
        } else {
            System.out.println("sorry, there was an error connecting\n");
            System.exit(1);
        }


        // reading in numbers (bytes) to be sent over the serial port
        System.out.println("type 'q' to end the example");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }

            try {
                input = in_stream.readLine();
                if (input.equals("q")) {
                    System.out.println("example terminated\n");
                    network.disconnect();
                    System.exit(0);
                }
            } catch (NumberFormatException ex) {
                System.out.println("please enter a correct number");
            } catch (IOException e) {
                System.out.println("there was an input error");
            }
        }
    }

    /**
     * Implementing {@link net.Network_iface#networkDisconnected(int)}, which is
     * called when the connection has been closed. In this example, the program
     * is ended.
     *
     * @see net.Network_iface
     */
    public void networkDisconnected(int id) {
        System.exit(0);
    }

    /**
     * Implementing {@link net.Network_iface#parseInput(int, int, int[])} to
     * handle messages received over the serial port. In this example, the
     * received bytes are written to command line (0 to 254) and the message is
     * sent back over the same serial port.
     *
     * @see net.Network_iface
     */
    public void parseInput(int id, int numBytes, int[] message) {
        System.out.print("received the following message: ");
        System.out.print(message[0]);
        for (int i = 1; i < numBytes; ++i) {
            System.out.print(", ");
            System.out.print(message[i]);
        }
        System.out.println();
    }

    /**
     * Implementing {@link net.Network_iface#writeLog(int, String)}, which is
     * used to write information concerning the connection. In this example, all
     * the information is simply written out to command line.
     *
     * @see net.Network_iface
     */
    public void writeLog(int id, String text) {
        System.out.println(text);
    }

}
