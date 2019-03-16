package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import model.Point;
import model.PointHolder;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Canvas menuCanvas;
    public Canvas paintCanvas;
    public ColorPicker colorPicker;
    public Slider thickness;
    public Label thicknessValue;
    public RadioButton clearModeButton;
    public ColorPicker backgroundColorPicker;
    private GraphicsContext graphicsContext;

    private int startPointX, startPointY;

    private Color backgroundColor;
    private Color linesColor;

    private PointHolder pointHolder;

    private boolean clearModeFlag;

    public void clear(ActionEvent actionEvent) {
        graphicsContext.clearRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        pointHolder.clearPoints();
        graphicsContext.setFill(backgroundColor);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
    }

    public void loadAndDrawImage(ActionEvent actionEvent) {
        Image image = load();
        drawImage(image);
    }

    private Image load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.jpg"));
        File loadImageFile = fileChooser.showOpenDialog(paintCanvas.getScene().getWindow());
        if (loadImageFile != null) {
            Image image = new Image(loadImageFile.toURI().toString(), paintCanvas.getWidth(), paintCanvas.getHeight(), false, false);
            return image;
        } else return null;
    }

    private void drawImage(Image image) {
        if (image != null) {
            PixelReader pixelReader = image.getPixelReader();
            double height = paintCanvas.getHeight() / image.getHeight();
            double width = paintCanvas.getWidth() / image.getWidth();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    Point p = new Point(color, x, y, thickness.getValue(), false);
                    pointHolder.addPoint(p);
                    graphicsContext.setFill(color);
                    graphicsContext.fillRect(x, y, width, height);
                }
            }
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        pointHolder = new PointHolder();
        graphicsContext = menuCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.DARKGOLDENROD);
        graphicsContext.fillRect(0, 0, menuCanvas.getWidth(), menuCanvas.getHeight());
        graphicsContext = paintCanvas.getGraphicsContext2D();
    }

    public void backgroundColorChanged(ActionEvent actionEvent) {
        graphicsContext.clearRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        backgroundColor = backgroundColorPicker.getValue();
        graphicsContext.setFill(backgroundColor);
        graphicsContext.fillRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());
        for (int i = 0; i < pointHolder.getCount() - 1; i++) {
            if (!(pointHolder.getPoint(i).isPointtLast()))
                graphicsContext.setStroke(pointHolder.getPoint(i).getColor());
            graphicsContext.setLineWidth(pointHolder.getPoint(i).getPointSize());
            graphicsContext.strokeLine(pointHolder.getPoint(i).getX(), pointHolder.getPoint(i).getY(), pointHolder.getPoint(i + 1).getX(), pointHolder.getPoint(i + 1).getY());
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        int endPointX = (int) mouseEvent.getX();
        int endPointY = (int) mouseEvent.getY();
        graphicsContext.setLineWidth(thickness.getValue());
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        if (clearModeFlag) {
            graphicsContext.setStroke(backgroundColor);
        } else {
            graphicsContext.setStroke(linesColor);
            pointHolder.addPoint(new Point(linesColor, startPointX, startPointY, thickness.getValue(), false));
        }
        graphicsContext.strokeLine(startPointX, startPointY, endPointX, endPointY);
        startPointX = endPointX;
        startPointY = endPointY;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        startPointX = (int) mouseEvent.getX();
        startPointY = (int) mouseEvent.getY();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        pointHolder.addPoint(new Point(linesColor, startPointX, startPointY, thickness.getValue(), true));
    }

    public void linesColorChanged(ActionEvent actionEvent) {
        linesColor = colorPicker.getValue();
        graphicsContext.setStroke(linesColor);
    }

    public void thicknessChanged(MouseEvent mouseEvent) {
        thicknessValue.setText(Double.toString(thickness.getValue()));
    }

    public void changeMode(ActionEvent actionEvent) {
        clearModeFlag = !clearModeFlag;
    }
}
