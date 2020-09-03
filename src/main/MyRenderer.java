package main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class MyRenderer extends DefaultTableCellRenderer {

    public int index;
    public final int startVal = 80;
    public final int gap = 20;
    public ArrayList<Color> mColors = new ArrayList<Color>();

    public MyRenderer(int _index){
        setHorizontalAlignment(SwingConstants.CENTER);
        index = _index;
        mColors.add(new Color(57,173,209)); // light blue
        mColors.add(new Color(48,121,171)); // dark blue
        mColors.add(new Color(194,89,117)); // mauve
        mColors.add(new Color(225,82,88)); // red
        mColors.add(new Color(249,132,91)); // orange
        mColors.add(new Color(131,140,199)); // lavender
        mColors.add(new Color(125,102,158)); // purple
        mColors.add(new Color(83,187,180)); // aqua
        mColors.add(new Color(81,180,109)); // green
        mColors.add(new Color(224,171,24)); // mustard
        mColors.add(new Color(99,122,145)); // dark gray
        mColors.add(new Color(240,146,176)); // pink
        mColors.add(new Color(183,192,199)); // light gray
        mColors.add(new Color(242,152,134));// coral
        mColors.add(new Color(141,25,43));// rose, 15개
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // 주소와 같이 숫자가 아닌 값들은 패스(비교조건이 숫자이기 때문)
        if(value.getClass().getSimpleName().equals("Integer")) {
            int val = (Integer)value;
            // 페이지를 나타내는 Column일 경우
            if (column == index) {
                cell.setForeground(null);
                if( val < mColors.size() && val > -1)
                    cell.setBackground(mColors.get(val));
                else {
                    cell.setForeground(new Color(255,255,255));
                    cell.setBackground(new Color(0, 0, 0));
                }
            } else {
                cell.setForeground(null);
                cell.setBackground(null);
            }
            return this;

        }
        else{
            cell.setForeground(null);
            cell.setBackground(null);
            return cell;
        }
    }
}
