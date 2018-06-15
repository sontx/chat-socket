package com.blogspot.sontx.chatsocket.server.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Ip addresses picker that will show all available addresses and the user can pick an address from the list.
 */
class AddressDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = -3712570364493836554L;
    private final JComboBox<String> ipList;
    private OnSelectedAddressListener mOnSelectedAddressListener = null;

    AddressDialog(JFrame owner) {
        super(owner);
        setResizable(false);
        setTitle("Available IP addresses");
        getContentPane().setLayout(null);

        ipList = new JComboBox<>();
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

    void setOnSelectedAddressListener(OnSelectedAddressListener listener) {
        mOnSelectedAddressListener = listener;
    }

    private List<String> getAllIPs() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        List<String> ips = new ArrayList<>();
        while (interfaces.hasMoreElements()) {
            NetworkInterface netInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object selectedItem = ipList.getSelectedItem();
        if (mOnSelectedAddressListener != null && selectedItem != null)
            mOnSelectedAddressListener.onSelectedAddress(selectedItem.toString());
        dispose();
    }

    public interface OnSelectedAddressListener {
        void onSelectedAddress(String address);
    }
}
