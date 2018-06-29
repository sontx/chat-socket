package com.blogspot.sontx.chatsocket.server.view.javafx;

import com.blogspot.sontx.chatsocket.AppConfig;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.utils.NetworkUtils;
import javafx.scene.control.ChoiceDialog;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class AddressDialog {
    @Setter
    private Callback<String> onSelectedAddress;

    public void show() {
        List<String> addresses = NetworkUtils.getAllAddresses();
        if (!addresses.isEmpty()) {
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(addresses.get(0), addresses);
            choiceDialog.setTitle(AppConfig.getDefault().getAppName());
            choiceDialog.setHeaderText("Available Addresses");
            Optional<String> result = choiceDialog.showAndWait();
            if (onSelectedAddress != null && result.isPresent())
                onSelectedAddress.call(result.get());
            choiceDialog.close();
        }
    }
}
