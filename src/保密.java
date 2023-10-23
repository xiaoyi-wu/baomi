package 系统;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import 图件.*;
import 工具.*;

class 保密 extends Frame implements WindowListener, ComponentListener, ActionListener {

  public static void main(String[] 参数) {
    new 保密(参数);
  }

  private 保密(String[] 参数) {
    super("保密");
    setLayout(new GridBagLayout());

    GridBagConstraints 左 = new GridBagConstraints();
    左.fill = GridBagConstraints.HORIZONTAL;
    GridBagConstraints 右 = new GridBagConstraints();
    右.fill = GridBagConstraints.HORIZONTAL;
    右.gridwidth = GridBagConstraints.REMAINDER;

    左.insets = new Insets(10, 10, 0, 0);
    右.insets = new Insets(10, 5, 0, 10);
    add(new Label("输入文件名：", Label.RIGHT), 左);
    add(输入 = new TextField(), 右);
    左.insets = new Insets(5, 10, 0, 0);
    右.insets = new Insets(5, 5, 0, 10);
    add(new Label("输出文件名：", Label.RIGHT), 左);
    add(输出 = new TextField(), 右);
    add(new Label("密码：", Label.RIGHT), 左);
    add(密码 = new TextField(), 右);
    add(new Label("确认：", Label.RIGHT), 左);
    add(核对 = new TextField(), 右);
    密码.setEchoChar('某');
    核对.setEchoChar('某');

    左.insets = new Insets(10, 10, 10, 0);
    add(new Label("文件格式：", Label.RIGHT), 左);
    CheckboxGroup 组合 = new CheckboxGroup();
    左.insets = new Insets(10, 5, 10, 0);
    add(new Checkbox("UTF-8文本", false, 组合), 左);
    add(格式 = new Checkbox("任意文件", true, 组合), 左);
    左.insets = new Insets(10, 40, 10, 40);
    add(开始 = new Button("加密/解密"), 左);

    pack();
    setResizable(false);
    Dimension 视窗 = getSize(), 屏幕 = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(位置 = new Point(屏幕.width - 视窗.width >> 1, 屏幕.height - 视窗.height >> 1));
    公告栏.setLocation(位置.x, 位置.y - 140);
    问讯处.setLocation(位置.x, 位置.y - 140);
    setVisible(true);

    addWindowListener(this);
    addComponentListener(this);
    输入.addActionListener(this);
    输出.addActionListener(this);
    密码.addActionListener(this);
    核对.addActionListener(this);
    开始.addActionListener(this);

    if (参数.length < 1) return;
    输入.setText(参数[0]);
    if (参数.length > 1) {
      输出.setText(参数[1]);
      密码.requestFocus();
    } else 输出.requestFocus();
  }

  public void windowOpened(WindowEvent 事件) {}
  public void windowClosed(WindowEvent 事件) {}
  public void windowIconified(WindowEvent 事件) {}
  public void windowDeiconified(WindowEvent 事件) {}
  public void windowActivated(WindowEvent 事件) {}
  public void windowDeactivated(WindowEvent 事件) {}
  public void windowClosing(WindowEvent 事件) {System.exit(0);}

  public void componentResized(ComponentEvent 事件) {}
  public void componentShown(ComponentEvent 事件) {}
  public void componentHidden(ComponentEvent 事件) {}

  public void componentMoved(ComponentEvent 事件) {
    Point 新家 = getLocation();
    公告栏.迁移(位置, 新家);
    问讯处.迁移(位置, 新家);
    位置 = 新家;
  }

  public void actionPerformed(ActionEvent 事件) {
    Object 源头 = 事件.getSource();
    输入名 = 输入.getText().trim();
    输出名 = 输出.getText().trim();
    口令 = 密码.getText();
    if (源头 == 输入 && !输入名.equals("")) 要求(输出);
    else if (源头 == 输出 && !输出名.equals("")) 要求(密码);
    else if (源头 == 密码 && !口令.equals("")) 要求(核对);
    else if (源头 == 核对) 开始.requestFocus();
    else if (源头 == 开始) {
      if (输入名.equals("")) 要求("请给输入文件名。", 输入);
      else if (输出名.equals("")) 要求("请给输出文件名。", 输出);
      else if (输出名.equals(输入名)) 要求("输出文件名应不同于输入文件名。", 输出);
      else if (口令.equals("")) 要求("请给密码，我一定保密。", 密码);
      else if (new File(输出名).exists()) {
        问讯处.询问("文件" + 输出名 + "已有，取代它么？");
        if (问讯处.答案() == 确认.是的) 加解密();
        else 要求(输出);
      } else 加解密();
    }
  }

  private void 要求(TextField 填写) {
    填写.selectAll();
    填写.requestFocus();
  }

  private void 要求(String 指令, TextField 填写) {
    公告栏.发送(指令);
    要求(填写);
  }

  private void 加解密() {
    try {
      InputStream 读出 = new FileInputStream(输入名);
      OutputStream 写入 = new FileOutputStream(输出名);
      byte[] 内容 = new byte [65536];
      boolean 任意 = 格式.getState();
      String 完成 = "解";
      if (读出.read(内容, 0, 16) < 16 || !是否解密(内容)) {
        读出.close();
        if (!核对.getText().equals(口令)) {
          要求("密码核对不正确。", 密码);
          return;
        } else 读出 = new FileInputStream(输入名);
        随机源.准备();
        for (int 甲 = 0; 甲 < 9; 甲++) 内容[甲] = (byte)('!' + 随机源.选择(94));
        内容[9] = (byte)((任意 ? '!' : '"') + 随机源.选择(47) * 2);
        随机源.准备(new String(内容, 0, 10));
        for (int 甲 = 10; 甲 < 16; 甲++) 内容[甲] = (byte)('!' + 随机源.选择(94));
        写入.write(内容, 0, 16);
        完成 = "加";
      } else 任意 = (内容[9] & 1) > 0;
      随机源.准备(new String(内容, 0, 10) + 口令);
      if (任意) for (int 数; (数 = 读出.read(内容)) > 0; 写入.write(内容, 0, 数))
        for (int 甲 = 0; 甲 < 数; 内容[甲++] ^= 随机源.选择(256));
      else for (int 余 = 0, 码, 甲, 数; (数 = 读出.read(内容, 余, 内容.length - 余)) > 0;) {
        for (数 += 余, 甲 = 0; 甲 < 数; 甲++) {
          if ((内容[甲] & 0x80) == 0) 内容[甲] = (byte)(码 = 加解密(内容[甲]));
          else if ((内容[甲] & 0xe0) == 0xc0) {
            if (甲 + 2 > 数) break;
            if ((内容[甲 + 1] & 0xc0) == 0x80) {
              码 = 加解密((内容[甲] & 0x1f) << 6 | 内容[甲 + 1] & 0x3f);
              内容[甲++] = (byte)(0xc0 | 码 >> 6);
              内容[甲] = (byte)(0x80 | 码 & 0x3f);
            } else 码 = -1;
          } else if ((内容[甲] & 0xf0) == 0xe0) {
            if (甲 + 3 > 数) break;
            if ((内容[甲 + 1] & 0xc0) == 0x80 && (内容[甲 + 2] & 0xc0) == 0x80) {
              码 = 加解密((内容[甲] & 0xf) << 12 | (内容[甲 + 1] & 0x3f) << 6 |
                内容[甲 + 2] & 0x3f);
              内容[甲++] = (byte)(0xe0 | 码 >> 12);
              内容[甲++] = (byte)(0x80 | 码 >> 6 & 0x3f);
              内容[甲] = (byte)(0x80 | 码 & 0x3f);
            } else 码 = -1;
          } else if ((内容[甲] & 0xf8) == 0xf0) {
            if (甲 + 4 > 数) break;
            if ((内容[甲 + 1] & 0xc0) == 0x80 && (内容[甲 + 2] & 0xc0) == 0x80 &&
              (内容[甲 + 3] & 0xc0) == 0x80) {
              码 = 加解密((内容[甲] & 7) << 18 | (内容[甲 + 1] & 0x3f) << 12 |
                (内容[甲 + 2] & 0x3f) << 6 | 内容[甲 + 3] & 0x3f);
              内容[甲++] = (byte)(0xf0 | 码 >> 18);
              内容[甲++] = (byte)(0x80 | 码 >> 12 & 0x3f);
              内容[甲++] = (byte)(0x80 | 码 >> 6 & 0x3f);
              内容[甲] = (byte)(0x80 | 码 & 0x3f);
            } else 码 = -1;
          } else 码 = -1;
          if (码 >= 0) continue;
          写入.close();
          读出.close();
          new File(输出名).delete();
          要求("输入文件不是UTF-8编码。", 输入);
          return;
        }
        写入.write(内容, 0, 甲);
        if ((余 = 数 - 甲) > 0) System.arraycopy(内容, 甲, 内容, 0, 余);
      }
      写入.close();
      读出.close();
      公告栏.发送(完成 + "密已完成。");
    } catch (Exception 错误) {
      公告栏.发送("有错误：" + 错误);
    }
  }

  private boolean 是否解密(byte[] 内容) {
    随机源.准备(new String(内容, 0, 10));
    for (int 甲 = 10; 甲 < 16; 甲++) if ('!' + 随机源.选择(94) != 内容[甲]) return false;
    return true;
  }

  private int 加解密(int 码) {
    if (码 >= 值域[语][0] && 码 <= 值域[语][1]) return 加解密(码, 值域[语]);
    for (int 甲 = 0; 甲 < 值域.length; 甲++) {
      if (码 < 值域[甲][0]) return 码;
      if (码 <= 值域[甲][1]) return 加解密(码, 值域[语 = 甲]);
    }
    return 码;
  }

  private int 加解密(int 码, int[] 域) {
    int 数 = 域[1] - 域[0] + 1;
    return 域[0] + (int)(域[1] + 随机源.选择(数) - 码) % 数;
  }

  private TextField 输入, 输出, 密码, 核对;
  private Checkbox 格式;
  private Button 开始;
  private String 输入名, 输出名, 口令;
  private 通知 公告栏 = new 通知(this);
  private 确认 问讯处 = new 确认(this);
  private 随机 随机源 = new 随机();
  private Point 位置;
  private int[][] 值域 = {{' ', '~'}, {0xa1, 0xff}, {0x400, 0x4ff}, {0x600, 0x6ff},
    {0x900, 0x97f}, {0x3041, 0x309f}, {0x30a0, 0x30ff}, {0x4e00, 0x9fff}, {0xac00, 0xd7a3}};
  private int 语;
}
