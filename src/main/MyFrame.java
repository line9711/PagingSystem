package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame{
    JFrame mainFrame;
    JPanel panel_txt;
    JPanel panel_table;
    JTable memory;
    JTable storage;
    JTable PMT;
    JLabel pageNumberLabel;
    JLabel pageSizeLabel;
    JScrollPane panel_memory;
    JScrollPane panel_storage;
    JScrollPane panel_PMT;
    DefaultTableModel model_memory;
    DefaultTableModel model_storage;
    DefaultTableModel model_PMT;
    JButton btn_input;
    JButton btn_delete;
    JTextField txt_pgFrame;
    JTextField txt_pgSize;
    final int MAX_PAGEFRAME = 15;
    final int MAX_PAGESIZE = 20;
    int cnt = 0;
    public MyFrame() {
     
    	mainFrame=new JFrame("Paging Simulation");
    	mainFrame.setLayout(new GridLayout(3,1));
    	

    	
    	mainFrame.setSize(600,1000);
    	mainFrame.setVisible(true);
    	mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
    	
    	
    	
    	//Memory 초기설정
        cnt = 0;
        String colNames_Memory[] = {"Main Memory"}; // Column 헤더 정의
        Object rowData_Memory[][] = {{-1}};
        model_memory = new DefaultTableModel(rowData_Memory, colNames_Memory);
        for (int cnt = 1; cnt < MAX_PAGEFRAME; cnt++) {
            model_memory.addRow(new Object[]{-1});
        }
        memory = new JTable(model_memory); // 데이터 모델을 테이블에 할당
        panel_memory = new JScrollPane(memory);
        panel_memory.setPreferredSize(new Dimension(150, (model_memory.getRowCount() + 2) * memory.getRowHeight()));
        memory.setEnabled(false);
        
        // Storage 초기설정
        cnt = 0;
        String colNames_storage[] = {"Storage Address", "Page Number"}; // Column 헤더 정의
        Object rowData_storage[][] = {{"0x"+Integer.toHexString(cnt), -1}};
        model_storage = new DefaultTableModel(rowData_storage, colNames_storage);
        for (cnt = 1; cnt < MAX_PAGESIZE; cnt++) {
            model_storage.addRow(new Object[]{"0x"+Integer.toHexString(cnt), -1});
        }
        storage = new JTable(model_storage); // 데이터 모델을 테이블에 할당
        panel_storage = new JScrollPane(storage);
        panel_storage.setPreferredSize(new Dimension(100, (model_storage.getRowCount() + 2) * storage.getRowHeight()));
        storage.setEnabled(false);

        // PMT 초기설정
        cnt = 0;
        String colNames_PMT[] = {"Page Number", "Residence Bit", "Storage Address", "Page Frame Number"}; // Column 헤더 정의
        Object rowData_PMT[][] = {{cnt,0,"0x"+Integer.toHexString(cnt),-1}};
        model_PMT = new DefaultTableModel(rowData_PMT, colNames_PMT);
        for (cnt = 1; cnt < MAX_PAGESIZE; cnt++) {
            model_PMT.addRow(new Object[]{cnt, 0, "0x"+Integer.toHexString(cnt), -1});
        }
        PMT = new JTable(model_PMT); // 데이터 모델을 테이블에 할당
        panel_PMT = new JScrollPane(PMT);
        panel_PMT.setPreferredSize(new Dimension(300, (model_PMT.getRowCount() + 2) * PMT.getRowHeight()));
        PMT.setEnabled(false);
        
        panel_table = new JPanel();
        panel_table.add(panel_memory);
        panel_table.add(panel_storage);
        

   
        // 텍스트 필드 정의
        txt_pgFrame = new JTextField(10);
        txt_pgSize = new JTextField(10);
        // 버튼 정의
        btn_input = new JButton("Input");
        btn_delete = new JButton("Delete");
        
        pageNumberLabel = new JLabel("PageFrame Number : ");
        pageSizeLabel = new JLabel("Page Size : ");
        
        panel_txt = new JPanel();
        panel_txt.setLayout(null);
        
        txt_pgFrame.setBounds(20,50,80,30);
        pageNumberLabel.setBounds(120,50,160,30);
        
        txt_pgSize.setBounds(320,50,80,30);
        pageSizeLabel.setBounds(420,50,160,30);
        
//      txt_pgSize.setBounds(370,50,80,30);
//      pageSizeLabel.setBounds(470,50,160,30);
        
        btn_input.setBounds(50,200,200,30);
        btn_delete.setBounds(350,200,200,30);
        
        btn_input.setBackground(Color.PINK);
        btn_delete.setBackground(Color.ORANGE);
        
        // input 버튼 리스너 추가
        btn_input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pgFrame = new Integer(txt_pgFrame.getText());
                int pgSize = new Integer(txt_pgSize.getText());
                insertData(pgFrame, pgSize);
            }
        });
        // delete 버튼 리스너 추가
        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pgFrame = new Integer(txt_pgFrame.getText());
                deleteData(pgFrame);
            }
        });
        
        memory.setDefaultRenderer(Object.class, new MyRenderer(0)); // 페이지 프레임은 첫번째인덱스
        storage.setDefaultRenderer(Object.class, new MyRenderer(1)); // 페이지 프레임은 두번째인덱스
        PMT.setDefaultRenderer(Object.class, new MyRenderer(3));

        panel_txt.add(txt_pgFrame);
        panel_txt.add(txt_pgSize);
        panel_txt.add(pageNumberLabel);
        panel_txt.add(pageSizeLabel);
        panel_txt.add(btn_input);
        panel_txt.add(btn_delete);
        

        mainFrame.add(panel_table);
        mainFrame.add(panel_txt);
        mainFrame.add(panel_PMT);



    	mainFrame.repaint();
    	mainFrame.revalidate();
    }
    
    
    public void insertData(int pgFrame, int pgSize) {

        // 기존과 중복되는 번호의 프레임이 있는지, 메모리가 꽉차있는지 검사
        if(isInvalid(pgFrame) == true){
            System.out.println("기존에 같은 번호의 페이지프레임이 있습니다");
            return;
        }
        if(isMemoryFull() == true){
            System.out.println("메모리가 가득 차있습니다");
            return;
        }
        if(isStorageOver(pgSize) == true){
            System.out.println("Storage 용량을 초과합니다");
            return;
        }
        for (int i = 0; i < MAX_PAGEFRAME; i++) {
            // 매개변수로 주어진 페이지프레임을 빈 메모리칸에 넣는다
            if ((Integer) model_memory.getValueAt(i, 0) == -1) {
                model_memory.setValueAt(pgFrame, i, 0);
                break;
            }
        }
        for (int i = 0, j = 0; i < MAX_PAGESIZE; i++) {
            // Residence Bit가 0이고, 페이지 갯수(j)보다 적게 할당했을때
            if ((Integer) model_PMT.getValueAt(i, 1) == 0 && j < pgSize ) {
                model_storage.setValueAt(pgFrame, i, 1); // 페이지가 속한 페이지프레임을 기록
                model_PMT.setValueAt(1, i, 1); // 현재 페이지을 채움
                model_PMT.setValueAt(pgFrame, i, 3); //
                j++;
            }
        }
    }
    // 텍스트필드에서 값을 읽어서 데이터모델에서 삭제하는 함수
    public void deleteData(int pgFrame) {
        for (int i = 0; i < MAX_PAGESIZE; i++) {
            // 매개변수로 주어진 페이지프레임이랑 같은 페이지는 Storage에서 모두 지운다
            if ((Integer) model_PMT.getValueAt(i, 3) == pgFrame) {
                model_storage.setValueAt(-1, i, 1);
                model_PMT.setValueAt(0, i, 1); // 현재 페이지을 비움
                model_PMT.setValueAt(-1, i, 3); // 속한 페이지프레임 번호를 -1로
            }
        }
        for (int i = 0; i < MAX_PAGEFRAME; i++) {
            // 매개변수로 주어진 페이지프레임을 메모리에서 지운다.
            if ((Integer) model_memory.getValueAt(i, 0) == pgFrame) {
                model_memory.setValueAt(-1, i, 0);
                break;
            }
        }

    }
    // 올바른 값인지 검사
    public boolean isInvalid(int pgFrame){
        // 이미 있는 프레임번호인지 확인(삽입 시 검사)
        for (int i = 0; i < MAX_PAGEFRAME; i++) {
            // 매개변수로 주어진 페이지프레임이랑 같은 페이지가 있는지 확인
            if ((Integer) model_memory.getValueAt(i, 0) == pgFrame) {
                return true; // 같은 페이지가 있다면 부적절한 값
            }
        }
        return false;
    }
    // Memory가 꽉차있는지 검사
    public boolean isMemoryFull(){
        // 메모리 전체를 검사
        for (int i = 0; i < MAX_PAGEFRAME; i++) {
            // 페이지프레임값이 -1인 칸이 있는지 검사
            if ((Integer) model_memory.getValueAt(i, 0) == -1) {
                return false; // -1인 값이 있다면 꽉차있지 않음
            }
        }
        return true; // 꽉차있음
    }
    // 삽입시 Storage가 초과되는지 검사
    public boolean isStorageOver(int pgSize){
        // Storage 전체를 검사
        int cnt = 0; // 빈 칸을 검사
        for (int i = 0; i < MAX_PAGESIZE; i++) {
            if ((Integer) model_storage.getValueAt(i, 1) == -1) {
                cnt++; // 빈 칸 갯수 증가
            }
        }
        // 빈 칸보다 새로 들어올 칸 수가 크다면 Over
        if( cnt < pgSize )
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        new MyFrame();
    }


}

