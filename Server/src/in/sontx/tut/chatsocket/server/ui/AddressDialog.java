package in.sontx.tut.chatsocket.server.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;

public class AddressDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -3712570364493836554L;
	private OnSelectedAddressListener mOnSelectedAddressListener = null;
	private JComboBox<String> ipList;

	public void setOnSelectedAddressListener(OnSelectedAddressListener listener) {
		mOnSelectedAddressListener = listener;
	}

	private List<String> getAllIPs() throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		List<String> ips = new ArrayList<>();
		while (interfaces.hasMoreElements()) {
			NetworkInterface netinterface = interfaces.nextElement();
			Enumeration<InetAddress> addresses = netinterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress address = addresses.nextElement();
				if (address.getAddress().length == 4)
					ips.add(address.getHostAddress());
			}
		}
		return ips;
	}

	private void loadIPsToUI() {
		try {
			List<String> ips = getAllIPs();
			for (String ip : ips) {
				ipList.addItem(ip);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public AddressDialog(JFrame owner) {
		super(owner);
		setResizable(false);
		setTitle("Available IP addresses");
		getContentPane().setLayout(null);

		ipList = new JComboBox<String>();
		ipList.setBounds(16, 11, 185, 20);
		loadIPsToUI();
		getContentPane().add(ipList);

		JButton btnTake = new JButton("Take");
		btnTake.setBounds(64, 42, 89, 23);
		btnTake.addActionListener(this);
		getContentPane().add(btnTake);

		setLocationRelativeTo(null);
		setSize(223, 105);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (mOnSelectedAddressListener != null)
			mOnSelectedAddressListener.onSelectedAddress(ipList.getSelectedItem().toString());
		dispose();
	}

	public interface OnSelectedAddressListener {
		void onSelectedAddress(String address);
	}
}
