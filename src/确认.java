package 图件;

import java.awt.*;
import java.awt.event.*;

public class 确认 extends Dialog implements ActionListener {

  public static final int 是的 = 0, 不是 = 1, 取消 = 2;

  public 确认(Frame 上级) {
    this(上级, "确认");
  }

  public 确认(Frame 上级, String 名称) {
    super(上级, 名称, true);
    setLayout(new GridBagLayout());
    GridBagConstraints 安排 = new GridBagConstraints();
    安排.gridwidth = GridBagConstraints.REMAINDER;
    安排.insets = new Insets(20, 20, 20, 20);
    add(标签 = new Label("", Label.CENTER), 安排);
    安排.gridwidth = 1;
    安排.weightx = 1;
    安排.insets = new Insets(0, 20, 10, 20);
    add(是 = new Button("是的"), 安排);
    安排.weightx = 0;
    安排.insets = new Insets(0, 0, 10, 0);
    add(否 = new Button("不是"), 安排);
    安排.weightx = 1;
    安排.insets = new Insets(0, 20, 10, 20);
    add(除 = new Button("取消"), 安排);
    是.addActionListener(this);
    否.addActionListener(this);
    除.addActionListener(this);
    setResizable(false);
    setLocation(200, 300);
  }

  public void 询问(String 问题) {
    标签.setText(问题);
    pack();
    setVisible(true);
  }

  public int 答案() {
    return 选择 == 是 ? 是的 : 选择 == 否 ? 不是 : 取消;
  }

  public void actionPerformed(ActionEvent 事件) {
    选择 = 事件.getSource();
    setVisible(false);
  }

  public void 迁移(Point 始, Point 终) {
    Point 点 = getLocation();
    setLocation(点.x + 终.x - 始.x, 点.y + 终.y - 始.y);
  }

  private Label 标签;
  private Button 是, 否, 除;
  private Object 选择;
}
