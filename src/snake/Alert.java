/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 *
 * @author Ceagan
 */
public class Alert {
    Alert(String header, String body){
        Dialog alert = new Dialog();
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.show();
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
    }
}
