package com.blogspot.sontx.chatsocket.client.view.swing;

import com.blogspot.sontx.chatsocket.client.view.ConnectionView;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.AbstractSwingWindow;
import com.blogspot.sontx.chatsocket.lib.view.FitImageJLabel;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ConnectionWindow extends AbstractSwingWindow implements ConnectionView, ActionListener {

    private JTextField portField;
    private JTextField ipField;
    @Setter
    private Runnable connectButtonClickListener;

    @Override
    protected void initializeComponents() {
        setResizable(false);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Address:");
        lblNewLabel.setBounds(384, 85, 43, 23);
        getContentPane().add(lblNewLabel);

        ipField = new JTextField();
        ipField.setBounds(437, 85, 99, 23);
        ipField.setText("localhost");
        ipField.addActionListener(this);
        getContentPane().add(ipField);
        ipField.setColumns(10);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(384, 119, 43, 23);
        getContentPane().add(lblPort);

        portField = new JTextField();
        portField.setBounds(437, 119, 99, 23);
        portField.setText("3393");
        portField.addActionListener(this);
        getContentPane().add(portField);
        portField.setColumns(10);

        JButton btnConnect = new JButton("Connect");
        btnConnect.setBounds(437, 153, 99, 23);
        btnConnect.addActionListener(this);
        getContentPane().add(btnConnect);

        FitImageJLabel bannerField = new FitImageJLabel();
        bannerField.setBounds(10, 11, 346, 242);
        bannerField.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("connection-banner.png")));
        getContentPane().add(bannerField);

        JLabel copyrightField = new JLabel("Copyright by sontx, www.sontx.in");
        copyrightField.setForeground(Color.GRAY);
        copyrightField.setBounds(0, 343, 300, 14);
        getContentPane().add(copyrightField);

        setSize(560, 397);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (connectButtonClickListener != null)
            connectButtonClickListener.run();
    }

    @Override
    public String getServerIp() {
        return ipField.getText();
    }

    @Override
    public String getServerPort() {
        return portField.getText();
    }

    @Override
    public void setServerIp(String serverIp) {
        ipField.setText(serverIp);
    }

    @Override
    public void setServerPort(int serverPort) {
        portField.setText(Integer.toString(serverPort));
    }
}
