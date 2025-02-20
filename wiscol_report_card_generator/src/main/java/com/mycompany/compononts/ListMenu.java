
package com.mycompany.compononts;

import com.mycompany.model.Model_Menu;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class ListMenu<E extends Object> extends JList<E>{

    private final DefaultListModel model;
    public ListMenu() {
        model = new DefaultListModel();
        setModel(model);
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
               Model_Menu data;
               if(value instanceof Model_Menu){
                   data=(Model_Menu) value;
               }else{
                   data = new Model_Menu("",value+"",Model_Menu.MenuType.EMPTY);
               }
               MenuItem item=new MenuItem(data);
               
               return item;
            }
            
        };
    }
    
    public void addItem(Model_Menu data){
        model.addElement(data);
    }  
        
}
