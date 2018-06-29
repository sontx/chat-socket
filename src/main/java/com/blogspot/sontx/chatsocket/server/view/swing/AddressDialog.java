package com.blogspot.sontx.chatsocket.server.view.swing;

import com.blogspot.sontx.chatsocket.lib.utils.NetworkUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private void loadIPsToUI() {
        List<String> ips = NetworkUtils.getAllAddresses();
        for (String ip : ips) {
            ipList.addItem(ip);
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
