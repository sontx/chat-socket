package com.blogspot.sontx.chatsocket.server.view.swing;

import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.view.BaseWindow;
import com.blogspot.sontx.chatsocket.server.view.LogView;
import com.blogspot.sontx.chatsocket.server.view.MainView;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log4j
class MainWindow extends BaseWindow implements ActionListener, MainView, LogView {
    private JTextField addressField;
    private JTextField portField;
    private JButton btnShowIPs;
    private JButton btnStart;

    private Runnable startButtonListener;
    private JTextPane logPane;

    @Override
    protected void initializeComponents() {
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel label = new JLabel("");
        label.setBounds(7, 11, 191, 294);
        label.setIcon(new ImageIcon(ImagesResource.getInstance().getImageByName("banner.png")));
        getContentPane().add(label);

        JPanel panel = new JPanel();
        panel.setBounds(200, 11, 313, 128);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblIp = new JLabel("IP:");
        lblIp.setBounds(10, 15, 94, 14);
        panel.add(lblIp);

        addressField = new JTextField();
        addressField.setBounds(114, 12, 156, 20);
        panel.add(addressField);
        addressField.setColumns(10);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(10, 43, 94, 14);
        panel.add(lblPort);

        portField = new JTextField();
        portField.setText("3393");
        portField.setBounds(114, 40, 156, 20);
        panel.add(portField);
        portField.setColumns(10);

        btnStart = new JButton("Start");
        btnStart.setBounds(214, 99, 89, 23);
        btnStart.addActionListener(this);
        panel.add(btnStart);

        btnShowIPs = new JButton("...");
        btnShowIPs.setBounds(280, 11, 25, 23);
        btnShowIPs.addActionListener(this);
        panel.add(btnShowIPs);

        JLabel lblCopyrightBySontx = new JLabel("Copyright by sontx, www.sontx.blogspot.com");
        lblCopyrightBySontx.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCopyrightBySontx.setForeground(Color.GRAY);
        lblCopyrightBySontx.setBounds(0, 321, 185, 14);
        getContentPane().add(lblCopyrightBySontx);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(208, 149, 297, 153);
        getContentPane().add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        logPane = new JTextPane();
        logPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel_1.add(scrollPane, BorderLayout.CENTER);

        setSize(529, 375);
        setLocationRelativeTo(null);
    }

    private void selectAddress() {
        AddressDialog dialog = new AddressDialog(this);
        dialog.setOnSelectedAddressListener(addressField::setText);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnShowIPs)) {
            selectAddress();
        } else if (e.getSource().equals(btnStart)) {
            if (startButtonListener != null)
                startButtonListener.run();
        }
    }

    @Override
    public String getIp() {
        return addressField.getText();
    }

    @Override
    public String getPort() {
        return portField.getText();
    }

    @Override
    public void setStartButtonText(String text) {
        btnStart.setText(text);
    }

    @Override
    public void setStartButtonListener(Runnable runnable) {
        this.startButtonListener = runnable;
    }

    @Override
    public synchronized void appendLog(String message) {
        String current = logPane.getText();
        if (StringUtils.isEmpty(current))
            logPane.setText(message);
        else
            logPane.setText(current + System.lineSeparator() + message);
    }

    @Override
    public void clearLog() {
        logPane.setText("");
    }
}
