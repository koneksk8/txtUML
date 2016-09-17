package hp;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Random;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import hp.model.Box;

public class BoxImpl extends Box {

	private OutputStream os;
	private ImagePanel virtualBox;

	public BoxImpl() {
		CommPortIdentifier portId = null;

		Enumeration<?> portEnum;
		try {
			portEnum = CommPortIdentifier.getPortIdentifiers();
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Please install rxtxSerial library and Arduino SDK. Check the readme file for details.");
			return;
		}

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println("Port found: " + currPortId.getName());
		}

		// First, Find an instance of serial port as set in PORT_NAMES.
		portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals("COM4") || currPortId.getName().equals("COM11")) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			initVirtualBox();
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			SerialPort serialPort = (SerialPort) portId.open(this.getClass().getName(), 2000);

			// set port parameters
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			os = serialPort.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// test();
	}

	private void test() {
		if (null == os) {
			return;
		}
		try {
			Random r = new Random();
			while (true) {
				int x = r.nextInt(9) + 48;
				os.write(x);
				os.flush();
				System.err.println("Sent: " + x);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void write(int i) {
		System.out.println("Sending: " + i + " ...");
		if (null == os) {
			initVirtualBox();
			virtualBox.setImage("box" + i + ".png");
			return;
		}
		try {
			os.write(i + '0');
			os.flush();
		} catch (Exception e) {
			os = null;
			initVirtualBox();
			virtualBox.setImage("box" + i + ".png");
			System.err.println(e.getMessage());
			return;
		}
		System.out.println("Sent: " + i);
	}

	private void initVirtualBox() {
		if (null == virtualBox) {
			virtualBox = new ImagePanel("box0.png");
			virtualBox.setVisible(true);
			System.out.println("Virtual magic box created.");
		}
	}

	@Override
	public void set9() {
		write(9);
	}

	@Override
	public void set5() {
		write(5);
	}

	@Override
	public void set0() {
		write(0);
	}

	@Override
	public void set6() {
		write(6);
	}

	@Override
	public void set7() {
		write(7);
	}

	@Override
	public void set2() {
		write(2);
	}

	@Override
	public void set4() {
		write(4);
	}

	@Override
	public void set3() {
		write(3);
	}

	@Override
	public void set8() {
		write(8);
	}

	@Override
	public void set1() {
		write(1);
	}
}
