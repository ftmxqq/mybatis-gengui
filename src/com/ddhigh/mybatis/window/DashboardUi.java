/**
 * Project Name:entityAuto
 * File Name:DashboardUi.java
 * Package Name:com.ddhigh.mybatis.window
 * Date:2016年11月25日下午3:28:27
 *
 */

package com.ddhigh.mybatis.window;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.mybatis.generator.exception.InvalidConfigurationException;

import com.ddhigh.mybatis.entity.ColumnEntity;
import com.ddhigh.mybatis.entity.DataBase;
import com.ddhigh.mybatis.entity.TableEntity;
import com.ddhigh.mybatis.util.CheckHeaderCellRenderer;
import com.ddhigh.mybatis.util.CheckTableModle;
import com.ddhigh.mybatis.util.DbUtil;
import com.ddhigh.mybatis.worker.GenerateWorker;
import com.ddhigh.mybatis.worker.GetTablesWorker;
import com.ddhigh.mybatis.worker.MemoryMonitorWorker;

/**
 * ClassName:DashboardUi <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月25日 下午3:28:27 <br/>
 *
 * @author gxp
 * @version
 * @since JDK 1.7
 * @see
 */
public class DashboardUi extends JFrame {

    private static Logger logger = Logger.getLogger(DashboardUi.class);
    private JPanel contentPane;
    private JTextField txtSrc;
    private JLabel label;
    private JTextField txtModelPkg;
    private JLabel lblxml;
    private JTextField txtMapPkg;
    private JLabel lblDao;
    private JTextField txtDaoPkg;
    private JLabel label_3;
    private JTextField txtEntity;
    private JLabel lblNewLabel;
    private JButton btnRefreshTable;
    private JButton btnGenerate;
    private JCheckBox checkBoxOverwrite;
    JLabel lblTable;
    int curd = 1;
    private JRadioButton rb_insert;
    private JRadioButton rb_update;
    private JRadioButton rb_del;
    private JRadioButton rb_select;
    private DbUtil dbUtil;

    //生成参数区域
    private String src;
    private String modelPkg;
    private String mapPkg;
    private String daoPkg;
    protected boolean overwrite = true;
    private String entitySuffix = "";
    JTextArea textArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DashboardUi frame = new DashboardUi(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DashboardUi(final DbUtil dbUtil) {
        this.dbUtil = dbUtil;
        setTitle("控制台");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 901, 610);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblSrc = new JLabel("src目录");
        lblSrc.setBounds(459, 10, 96, 21);
        contentPane.add(lblSrc);

        txtSrc = new JTextField();
        txtSrc.setText("请选择生成的src根目录");
        txtSrc.setEditable(false);
        txtSrc.setBounds(565, 10, 146, 21);
        contentPane.add(txtSrc);
        txtSrc.setColumns(10);

        JButton btnSrc = new JButton("浏览");
        btnSrc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(getContentPane());
                if (result == JFileChooser.APPROVE_OPTION) {
                    src = fileChooser.getSelectedFile().getAbsolutePath();
                    txtSrc.setText(src);
                }
            }
        });
        btnSrc.setBounds(718, 9, 73, 23);
        contentPane.add(btnSrc);

        label = new JLabel("实体包名");
        label.setBounds(459, 37, 96, 21);
        contentPane.add(label);

        txtModelPkg = new JTextField();
        txtModelPkg.setColumns(10);
        txtModelPkg.setBounds(565, 37, 146, 21);
        contentPane.add(txtModelPkg);

        lblxml = new JLabel("映射xml包名");
        lblxml.setBounds(459, 64, 96, 21);
        contentPane.add(lblxml);

        txtMapPkg = new JTextField();
        txtMapPkg.setColumns(10);
        txtMapPkg.setBounds(565, 64, 146, 21);
        contentPane.add(txtMapPkg);

        lblDao = new JLabel("Dao包名");
        lblDao.setBounds(459, 91, 96, 21);
        contentPane.add(lblDao);

        txtDaoPkg = new JTextField();
        txtDaoPkg.setColumns(10);
        txtDaoPkg.setBounds(565, 91, 146, 21);
        contentPane.add(txtDaoPkg);

        label_3 = new JLabel("实体后缀");
        label_3.setBounds(459, 118, 96, 21);
        contentPane.add(label_3);

        txtEntity = new JTextField();
        txtEntity.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                entitySuffix = txtEntity.getText().trim();
                logger.info("entitySuffix => " + entitySuffix);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                entitySuffix = txtEntity.getText().trim();
                logger.info("entitySuffix => " + entitySuffix);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                entitySuffix = txtEntity.getText().trim();
                logger.info("entitySuffix => " + entitySuffix);
            }
        });
        txtEntity.setColumns(10);
        txtEntity.setBounds(565, 118, 146, 21);
        contentPane.add(txtEntity);

        lblNewLabel = new JLabel("更改后重新加载");
        lblNewLabel.setBounds(565, 149, 96, 21);
        contentPane.add(lblNewLabel);

        checkBoxOverwrite = new JCheckBox("存在则覆盖");
        checkBoxOverwrite.setSelected(true);
        checkBoxOverwrite.setBounds(565, 176, 103, 23);
        contentPane.add(checkBoxOverwrite);

        btnRefreshTable = new JButton("重新加载");
        btnRefreshTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchTableLoader();
            }
        });
        btnRefreshTable.setBounds(516, 221, 93, 23);
        contentPane.add(btnRefreshTable);

        btnGenerate = new JButton("生成");
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generate();
            }
        });
        btnGenerate.setBounds(618, 221, 93, 23);
        contentPane.add(btnGenerate);

        labelStatus = new JLabel("xx");
        labelStatus.setBounds(10, 545, 524, 21);
        contentPane.add(labelStatus);

        labelMemory = new JLabel("");
        labelMemory.setBounds(565, 545, 236, 21);
        contentPane.add(labelMemory);

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setBounds(10, 9, 439, 272);
        contentPane.add(scrollPane);

        tableTable = new JTable();
        tableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableTable.getSelectedRow();
                    String tb = (String) tableTable.getValueAt(row, 1);
                    lblTable.setText(tb);
                    updateCt(tb);
                }
            }
        });
        scrollPane.setViewportView(tableTable);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 309, 270, 221);
        contentPane.add(scrollPane_1);

        tableS = new JTable();
        scrollPane_1.setViewportView(tableS);

        scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(290, 309, 270, 221);
        contentPane.add(scrollPane_2);

        tableW = new JTable();
        scrollPane_2.setViewportView(tableW);

        lblTable = new JLabel("表");
        lblTable.setBounds(10, 291, 201, 15);
        contentPane.add(lblTable);

        rb_insert = new JRadioButton("插入");
        rb_insert.setBounds(563, 320, 64, 23);
        contentPane.add(rb_insert);
        rb_insert.setSelected(true);

        rb_update = new JRadioButton("更新");
        rb_update.setBounds(565, 358, 64, 23);
        contentPane.add(rb_update);

        rb_del = new JRadioButton("删除");
        rb_del.setBounds(565, 394, 64, 23);
        contentPane.add(rb_del);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rb_del);
        bg.add(rb_update);
        bg.add(rb_insert);

        JButton btn_sql = new JButton("产生sql");
        btn_sql.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (rb_insert.isSelected()) {
                    curd = 1;
                } else if (rb_update.isSelected()) {
                    curd = 2;
                } else if (rb_del.isSelected()) {
                    curd = 3;
                } else {
                    curd = 4;
                }
                genSql(curd);
            }
        });
        btn_sql.setBounds(565, 479, 93, 23);
        contentPane.add(btn_sql);

        JScrollPane scrollPane_3 = new JScrollPane();
        scrollPane_3.setBounds(670, 291, 205, 239);
        contentPane.add(scrollPane_3);

        textArea = new JTextArea();
        textArea.setTabSize(10);
        textArea.setLineWrap(true);//激活自动换行功能
        textArea.setWrapStyleWord(true);//激活断行不断字功能
        scrollPane_3.setViewportView(textArea);

        rb_select = new JRadioButton("选择");
        bg.add(rb_select);
        rb_select.setBounds(565, 430, 64, 23);
        contentPane.add(rb_select);
        setVisible(true);
        //启动监控线程
        launchMonitor();
        //首次加载表格
        launchTableLoader();
    }

    /**
     * 启动监控线程
     */
    private void launchMonitor() {
        MemoryMonitorWorker worker = new MemoryMonitorWorker(new MemoryMonitorWorker.OnMemoryLoadedListener() {
            @Override
            public void onLoaded(long used, long free, long total) {
                labelMemory.setText(used / 1024 / 1024 + "MB/" + total / 1024 / 1024 + "MB " + used * 100 / total + "%");
            }
        });
        worker.execute();
        logger.info("监控线程启动");
    }

    /**
     * 加载数据表
     */
    private void launchTableLoader() {
        btnRefreshTable.setEnabled(false);
        btnGenerate.setEnabled(false);
        GetTablesWorker getTablesWorker = new GetTablesWorker(dbUtil);
        getTablesWorker.setListener(new GetTablesWorker.OnLoadedListener() {
            @Override
            public void onSuccess(DataBase dataBase) {
                btnRefreshTable.setEnabled(true);
                btnGenerate.setEnabled(true);
                labelStatus.setText("成功加载【" + dataBase.getTables().size() + "】张数据表");
                tables = dataBase.getTables();
                displayTable();
            }

            @Override
            public void onError(String message, Throwable ex) {
                labelStatus.setText(message + " " + ex.getMessage());
                logger.error(message, ex);
                btnRefreshTable.setEnabled(true);
            }
        });
        getTablesWorker.execute();
    }

    private static Vector<String> tableColumnNames = new Vector<>();

    static {
        tableColumnNames.add("选择");
        tableColumnNames.add("表名");
        tableColumnNames.add("实体类名");
        tableColumnNames.add("注释");
    }

    private List<TableEntity> tables;
    private JLabel labelStatus;
    private JLabel labelMemory;
    private JTable tableTable;
    private JScrollPane scrollPane_1;
    private JTable tableS;
    private JScrollPane scrollPane_2;
    private JTable tableW;

    /**
     * 通过数据加载table
     */
    private void displayTable() {
        for (TableEntity t : tables) {
            t.setEntityName(t.getEntityName() + entitySuffix);
        }
        Vector data = new Vector();
        for (TableEntity t : tables) {
            Vector vc = new Vector();
            vc.add(false);
            vc.add(t.getTableName());
            vc.add(t.getEntityName());
            vc.add(t.getComment());
            data.add(vc);
        }
        CheckTableModle tableModel = new CheckTableModle(data, tableColumnNames);
        tableTable.setModel(tableModel);
        tableTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(tableTable));
        //        tableTable.setModel(new TableModel() {
        //            @Override
        //            public int getRowCount() {
        //                return tables.size();
        //            }
        //
        //            @Override
        //            public int getColumnCount() {
        //                return tableColumnNames.size();
        //            }
        //
        //            @Override
        //            public String getColumnName(int columnIndex) {
        //                return tableColumnNames.get(columnIndex);
        //            }
        //
        //            @Override
        //            public Class<?> getColumnClass(int columnIndex) {
        //                return String.class;
        //            }
        //
        //            @Override
        //            public boolean isCellEditable(int rowIndex, int columnIndex) {
        //                return columnIndex == 2;
        //            }
        //
        //            @Override
        //            public Object getValueAt(int rowIndex, int columnIndex) {
        //                TableEntity entity = tables.get(rowIndex);
        //                if (columnIndex == 0) {
        //                    return rowIndex + 1;
        //                }
        //                if (columnIndex == 1) {
        //                    return entity.getTableName();
        //                }
        //                if (columnIndex == 2) {
        //                    return entity.getEntityName();
        //                }
        //                if (columnIndex == 3) {
        //                    return entity.getComment();
        //                }
        //                return null;
        //            }
        //
        //            @Override
        //            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //                if (columnIndex == 2) {
        //                    TableEntity entity = tables.get(rowIndex);
        //                    entity.setEntityName(aValue.toString());
        //                    logger.debug("[" + rowIndex + "][" + columnIndex + "] - " + aValue);
        //                }
        //            }
        //
        //            @Override
        //            public void addTableModelListener(TableModelListener l) {
        //
        //            }
        //
        //            @Override
        //            public void removeTableModelListener(TableModelListener l) {
        //
        //            }
        //        });
        TableColumn tableColumn = tableTable.getColumnModel().getColumn(0);
        tableColumn.setMaxWidth(48);
        tableColumn.setPreferredWidth(48);
        tableColumn.setMinWidth(48);
        tableTable.validate();
    }

    private void updateCt(String tb) {
        List<ColumnEntity> lc = null;
        for (TableEntity t : tables) {
            if (t.getTableName().equals(tb)) {
                lc = t.getColumns();
            }
        }
        Vector data = new Vector();
        for (ColumnEntity c : lc) {
            Vector vc = new Vector();
            vc.add(false);
            vc.add(c.getName());
            vc.add(c.getComment());
            data.add(vc);
        }
        Vector<String> cnames = new Vector<String>();
        cnames.add("Set列");
        cnames.add("列名");
        cnames.add("注释");

        CheckTableModle tableModel = new CheckTableModle(data, cnames);
        tableS.setModel(tableModel);
        tableS.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(tableS));

        TableColumn tableColumn = tableS.getColumnModel().getColumn(0);
        tableColumn.setMaxWidth(68);
        tableColumn.setPreferredWidth(68);
        tableColumn.setMinWidth(68);
        tableS.validate();

        Vector<String> wnames = new Vector<String>();
        wnames.add("Where列");
        wnames.add("列名");
        wnames.add("注释");
        Vector dataw = new Vector();
        for (ColumnEntity c : lc) {
            Vector vc = new Vector();
            vc.add(false);
            vc.add(c.getName());
            vc.add(c.getComment());
            dataw.add(vc);
        }
        CheckTableModle tableModelw = new CheckTableModle(dataw, wnames);
        tableW.setModel(tableModelw);
        tableW.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(tableW));

        TableColumn tableColumnw = tableW.getColumnModel().getColumn(0);
        tableColumnw.setMaxWidth(68);
        tableColumnw.setPreferredWidth(68);
        tableColumnw.setMinWidth(68);
        tableW.validate();
    }

    /**
     * 生成XML
     */
    private void generate() {
        src = txtSrc.getText().trim();
        modelPkg = txtModelPkg.getText().trim();
        mapPkg = txtMapPkg.getText().trim();
        daoPkg = txtDaoPkg.getText().trim();
        overwrite = checkBoxOverwrite.isSelected();
        entitySuffix = txtEntity.getText().trim();
        if (src.equals("请选择生成的src根目录") || modelPkg.isEmpty() || mapPkg.isEmpty() || daoPkg.isEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "请将信息填写完整");
            return;
        }
        labelStatus.setText("生成中");
        btnGenerate.setEnabled(false);
        btnRefreshTable.setEnabled(false);
        List<String> tbs = new ArrayList<String>();
        for (int i = 0; i < tableTable.getRowCount(); i++) {
            if ((Boolean) tableTable.getValueAt(i, 0)) {
                tbs.add((String) tableTable.getValueAt(i, 1));
            }
        }
        List<TableEntity> ts = new ArrayList<TableEntity>();
        for (TableEntity te : tables) {
            if (tbs.contains(te.getTableName())) {
                ts.add(te);
            }
        }
        try {
            GenerateWorker worker = new GenerateWorker(src, modelPkg, mapPkg, daoPkg, ts, labelStatus, overwrite, dbUtil);
            worker.setListener(new GenerateWorker.OnGenerateCompleteListener() {
                @Override
                public void onSuccess(String msg) {
                    labelStatus.setText(msg);
                    btnGenerate.setEnabled(true);
                    btnRefreshTable.setEnabled(true);

                    //打开生成后文件夹
                    String[] cmd = new String[5];
                    cmd[0] = "cmd";
                    cmd[1] = "/c";
                    cmd[2] = "start";
                    cmd[3] = " ";
                    cmd[4] = src;
                    try {
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }

                @Override
                public void onError(String message, Throwable ex) {
                    labelStatus.setText(message);
                    btnGenerate.setEnabled(true);
                    btnRefreshTable.setEnabled(true);
                    logger.error(message, ex);
                }
            });
            worker.execute();
        } catch (InterruptedException | InvalidConfigurationException | SQLException | IOException e) {
            logger.error(e);
            labelStatus.setText(e.getMessage());
            btnGenerate.setEnabled(true);
            btnRefreshTable.setEnabled(true);
        }
    }

    public void genSql(int flag) {
        textArea.setText("");
        String table = lblTable.getText().trim();
        StringBuilder sb = new StringBuilder();
        List<String> scl = new ArrayList<String>();
        List<String> wcl = new ArrayList<String>();
        if (true) {
            for (int i = 0; i < tableS.getRowCount(); i++) {
                if ((Boolean) tableS.getValueAt(i, 0)) {
                    scl.add((String) tableS.getValueAt(i, 1));
                }
            }
            for (int i = 0; i < tableW.getRowCount(); i++) {
                if ((Boolean) tableW.getValueAt(i, 0)) {
                    wcl.add((String) tableW.getValueAt(i, 1));
                }
            }
        }
        if (flag == 1) {//insert

            if (tableS.getRowCount() < 1) {
                JOptionPane.showMessageDialog(getContentPane(), "请双击选择表");
                return;
            } else {
                sb.append("insert into ").append(table).append("(");
                StringBuilder sqlsb = new StringBuilder();
                for (int i = 0; i < tableS.getRowCount(); i++) {
                    sqlsb.append(",").append(((String) tableS.getValueAt(i, 1)).toUpperCase());
                }
                sb.append(sqlsb.toString().substring(1));
                sb.append(") values (");
                sqlsb.setLength(0);
                for (int i = 0; i < tableS.getRowCount(); i++) {
                    sqlsb.append(",").append("#{").append(((String) tableS.getValueAt(i, 1)).toLowerCase()).append("}");
                }
                sb.append(sqlsb.toString().substring(1)).append(")");
            }
        } else if (flag == 2) {//update
            if (scl.size() == 0 || wcl.size() == 0) {
                JOptionPane.showMessageDialog(getContentPane(), "请双击选择set和where列");
                return;
            } else {
                sb.append("update ").append(table).append(" set ");
                StringBuilder sqlsb = new StringBuilder();
                for (int i = 0; i < scl.size(); i++) {

                    sqlsb.append(",").append(scl.get(i).toUpperCase()).append("=#{").append(scl.get(i)).append("}");
                }
                sb.append(sqlsb.toString().substring(1));
                sb.append(" where ");
                sqlsb.setLength(0);
                for (int i = 0; i < wcl.size(); i++) {
                    if (i == 0) {
                        sqlsb.append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                    } else {
                        sqlsb.append(" and ").append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                    }

                }
                sb.append(sqlsb.toString());
            }
        } else if (flag == 3) {//del
            sb.append("delete ");
            StringBuilder sqlsb = new StringBuilder();
            sb.append(" from ").append(table);
            sqlsb.setLength(0);
            for (int i = 0; i < wcl.size(); i++) {
                if (i == 0) {

                    sqlsb.append(" where ").append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                } else {
                    sqlsb.append(" and ").append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                }

            }
            sb.append(sqlsb.toString());
        } else {//select
            sb.append("select ");
            StringBuilder sqlsb = new StringBuilder();
            if (scl.size() < 1) {
                for (int i = 0; i < tableS.getRowCount(); i++) {
                    sqlsb.append(",").append(((String) tableS.getValueAt(i, 1)).toUpperCase());
                }
            } else {
                for (int i = 0; i < scl.size(); i++) {

                    sqlsb.append(",").append(scl.get(i).toUpperCase());
                }
            }

            sb.append(sqlsb.toString().substring(1));
            sb.append(" from ").append(table);
            sqlsb.setLength(0);
            for (int i = 0; i < wcl.size(); i++) {
                if (i == 0) {

                    sqlsb.append(" where ").append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                } else {
                    sqlsb.append(" and ").append(wcl.get(i).toUpperCase()).append("=#{").append(wcl.get(i)).append("}");
                }

            }
            sb.append(sqlsb.toString());
        }
        textArea.setText(sb.toString());

    }
}
