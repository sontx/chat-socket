package com.blogspot.sontx.chatsocket.client.view.javafx.custom;

import com.blogspot.sontx.chatsocket.lib.bean.Profile;
import com.blogspot.sontx.chatsocket.lib.bo.ImagesResource;
import com.blogspot.sontx.chatsocket.lib.bo.LayoutsResource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

@Log4j
public class FriendListViewCell extends ListCell<Profile> {
    @FXML
    private Label displayNameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Node root;

    public FriendListViewCell() {
        initializeLoader();
    }

    private void initializeLoader() {
        FXMLLoader loader = new FXMLLoader(LayoutsResource.getInstance().getResource("friend-list-view-cell.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            log.error("Error while showing friend listview cell", e);
        }
    }

    @Override
    protected void updateItem(Profile item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            displayNameLabel.setText("");
            statusLabel.setText("");
            avatarImageView.setImage(null);
        } else {
            displayNameLabel.setText(item.getDisplayName());
            statusLabel.setText(item.getStatus());
            String imageName = item.isOnline() ? "online.png" : "offline.png";
            avatarImageView.setImage(new Image(ImagesResource.getInstance().getImageAsUrl(imageName).toString()));
        }
        setText(null);
        setGraphic(root);
    }
}
