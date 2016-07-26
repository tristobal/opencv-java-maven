package cl.tristobal.opencv;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class FrameGrabberRunnable implements Runnable {
    private VideoCapture videoCapture;

    private ImageView imageView;

    private static Logger logger = LogManager.getLogger(FrameGrabberRunnable.class);

    public FrameGrabberRunnable(VideoCapture capture, ImageView image) {
        videoCapture = capture;
        imageView = image;
    }

    public void run() {
        Image imageToShow = grabFrame();
        imageView.setImage(imageToShow);
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Image grabFrame() {
        Image imageToShow = null;
        Mat frame = new Mat();

        if (videoCapture.isOpened()) {
            try {
                videoCapture.read(frame);
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                    imageToShow = matObjectToImageJavaFX(frame);
                }

            } catch (Exception e) {
                logger.error("Exception during the image elaboration", e);
            }
        }

        return imageToShow;
    }

    /**
     * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
     *
     * @param frame the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    private Image matObjectToImageJavaFX(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
