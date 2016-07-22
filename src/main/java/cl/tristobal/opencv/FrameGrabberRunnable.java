package cl.tristobal.opencv;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class FrameGrabberRunnable implements Runnable {
    private VideoCapture capture;

    private ImageView currentFrame;

    public FrameGrabberRunnable(VideoCapture videoCapture, ImageView imageView) {
        capture = videoCapture;
        currentFrame = imageView;
    }

    public void run() {
        Image imageToShow = grabFrame();
        currentFrame.setImage(imageToShow);
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Image grabFrame() {
        Image imageToShow = null;
        Mat frame = new Mat();

        // check if the capture is open
        if (capture.isOpened()) {
            try {
                // read the current frame
                capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // convert the image to gray scale
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                    // convert the Mat object (OpenCV) to Image (JavaFX)
                    imageToShow = mat2Image(frame);
                }

            } catch (Exception e) {
                System.err.println("Exception during the image elaboration: " + e);
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
    private Image mat2Image(Mat frame) {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
