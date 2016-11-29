/**
 * Project Name:entityAuto
 * File Name:LoginWindowUi.java
 * Package Name:com.ddhigh.mybatis.window
 * Date:2016年11月25日上午11:27:43
 *
 */

package com.ddhigh.mybatis.window;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.ddhigh.mybatis.entity.DataBase;
import com.ddhigh.mybatis.util.DbUtil;
import com.ddhigh.mybatis.worker.GetTablesWorker;

/**
 * ClassName:LoginWindowUi <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月25日 上午11:27:43 <br/>
 *
 * @author gxp
 * @version
 * @since JDK 1.7
 * @see
 */
public class LoginWindowUi extends JFrame {
    private final JComboBox comboBoxType;
    private final JButton btnConnect;
    private JPanel contentPane;
    private final JTextField txtHost;
    private final JTextField txtPort;
    private final JTextField txtUsername;
    private final JTextField txtPassword;
    private final JTextField txtDatabase;
    private static Logger logger = Logger.getLogger(LoginWindowUi.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginWindowUi frame = new LoginWindowUi();
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
    public LoginWindowUi() {
        setTitle("连接数据库");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 250, 262);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("类型");
        lblNewLabel.setBounds(0, 10, 37, 15);
        contentPane.add(lblNewLabel);

        comboBoxType = new JComboBox();
        comboBoxType.setModel(new DefaultComboBoxModel(new String[] { "MySQL", "Oracle" }));
        comboBoxType.setBounds(60, 10, 164, 21);
        contentPane.add(comboBoxType);

        JLabel label = new JLabel("主机");
        label.setBounds(0, 49, 54, 15);
        contentPane.add(label);

        txtHost = new JTextField();
        txtHost.setText("localhost");
        txtHost.setBounds(60, 46, 164, 21);
        contentPane.add(txtHost);
        txtHost.setColumns(10);

        JLabel label_1 = new JLabel("端口");
        label_1.setBounds(0, 77, 54, 15);
        contentPane.add(label_1);

        txtPort = new JTextField();
        txtPort.setText("3306");
        txtPort.setColumns(10);
        txtPort.setBounds(60, 74, 164, 21);
        contentPane.add(txtPort);

        JLabel label_2 = new JLabel("账号");
        label_2.setBounds(0, 108, 54, 15);
        contentPane.add(label_2);

        txtUsername = new JTextField();
        txtUsername.setText("root");
        txtUsername.setColumns(10);
        txtUsername.setBounds(60, 105, 164, 21);
        contentPane.add(txtUsername);

        JLabel label_3 = new JLabel("密码");
        label_3.setBounds(0, 136, 54, 15);
        contentPane.add(label_3);

        txtPassword = new JTextField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(60, 133, 164, 21);
        contentPane.add(txtPassword);

        JLabel label_4 = new JLabel("数据库");
        label_4.setBounds(0, 164, 54, 15);
        contentPane.add(label_4);

        txtDatabase = new JTextField();
        txtDatabase.setColumns(10);
        txtDatabase.setBounds(60, 161, 164, 21);
        contentPane.add(txtDatabase);

        btnConnect = new JButton("连接数据库");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = txtHost.getText().trim();
                String port = txtPort.getText().trim();
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();
                String type = comboBoxType.getSelectedItem().toString();
                String database = txtDatabase.getText().trim();
                if (host.isEmpty() || port.isEmpty() || username.isEmpty() || database.isEmpty()) {
                    JOptionPane.showMessageDialog(getContentPane(), "请填写必填信息");
                    return;
                }
                connect(host, port, username, password, type, database);
            }
        });
        btnConnect.setBounds(59, 192, 115, 23);
        contentPane.add(btnConnect);
    }

    private void connect(final String host, final String port, final String username, final String password, final String type, final String database) {
        btnConnect.setText("连接中");
        btnConnect.setEnabled(false);
        GetTablesWorker getTablesWorker = new GetTablesWorker(host, port, username, password, type, database);
        getTablesWorker.setListener(new GetTablesWorker.OnLoadedListener() {
            @Override
            public void onSuccess(DataBase dataBase) {
                DbUtil.Type dbType;
                switch (type) {
                case "Oracle":
                    dbType = DbUtil.Type.Oracle;
                    break;
                default:
                    dbType = DbUtil.Type.MySQL;
                    break;
                }
                new DashboardUi(new DbUtil(host, port, username, password, dbType, database));
                dispose();
            }

            @Override
            public void onError(String message, Throwable ex) {
                btnConnect.setEnabled(true);
                btnConnect.setText("连接");
                JOptionPane.showMessageDialog(getContentPane(), message);
                logger.error(message, ex);
            }
        });
        getTablesWorker.execute();
    }
}
