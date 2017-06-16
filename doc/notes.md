# Tray Icon

## How to change icon

```java
Image image = Toolkit.getDefaultToolkit().getImage( getClass().getResource("images/icon2.png") );
_trayIcon.setImage(image);
```


## Popup message in Tray Icon

```java
_trayIcon.displayMessage("Tray Test", "This is a message for testing pop-up", MessageType.INFO);
```


# How to paste the image from a clipboard in browser
http://jsfiddle.net/KJW4E/905/

