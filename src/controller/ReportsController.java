package controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class ReportsController {

    private Stage stage;

    public ReportsController()
    {
    }

    @FXML
    private void initialize()
    {
    }


    public static void setColumns(TableView tableView, List<TableColumn> columns, MenuButton menuButton)
    {
        if(tableView==null)
            tableView = new TableView();
        tableView.getColumns().clear();
        if(((CheckMenuItem)menuButton.getItems().get(0)).isSelected())
        {
            for(int i = 0; i<columns.size();i++) {
                tableView.getColumns().add(columns.get(i));
            }
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            return;
        }
        for(int i = 1; i<menuButton.getItems().size();i++)
        {
            if(((CheckMenuItem)menuButton.getItems().get(i)).isSelected()){
                tableView.getColumns().add(columns.get(i-1));
            }
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if(tableView.getColumns().size()==0)
        {
            for(int i = 0; i<columns.size();i++) {
                tableView.getColumns().add(columns.get(i));
            }
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }

    public static List<String> selectedMenuItemsToStringList(List<MenuItem> items)
    {
        List<String> result = new ArrayList<>();
        for(MenuItem m : items)
        {
            CheckMenuItem item = (CheckMenuItem)m;
            if(item.isSelected())
                result.add(m.getText());
        }
        return result;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
}