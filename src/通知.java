package 图件;

import java.awt.*;
import java.awt.event.*;

public class 通知 extends Dialog implements ActionListener {

  public 通知(Frame 上级) {
    this(上级, "通知");
  }

  public 通知(Frame 上级, String 名称) {
    super(上级, 名称, true);
    setLayout(new GridBagLayout());
    GridBagConstraints 安排 = new GridBagConstraints();
    安排.gridwidth = GridBagConstraints.REMAINDER;
    安排.insets = new Insets(20, 20, 20, 20);
    add(标签 = new Label("", Label.CENTER), 安排);
    安排.insets = new Insets(0, 10, 10, 10);
    Button 好;
    add(好 = new Button("好的"), 安排);
    好.addActionListener(this);
    setResizable(false);
    setLocation(200, 300);
  }

  public void 发送(String 消息) {
    标签.setText(消息);
    pack();
    setVisible(true);
  }

  public void actionPerformed(ActionEvent 事件) {
    setVisible(false);
  }

  public void 迁移(Point 始, Point 终) {
    Point 点 = getLocation();
    setLocation(点.x + 终.x - 始.x, 点.y + 终.y - 始.y);
  }

  private Label 标签;
}
