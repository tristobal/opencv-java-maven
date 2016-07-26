package cl.tristobal.opencv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private static Logger logger = LogManager.getLogger(Controller.class);

    @FXML
    private Button cameraButton;

    @FXML
    private ImageView currentFrame;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();

    private boolean isNotCameraActive = true;

    /**
     * The action triggered by pushing the button on the GUI
     *
     * @param event the push button event
     */
    @FXML
    protected void startCamera(ActionEvent event) {

        if (isNotCameraActive) {
            // start the video capture
            capture.open(0);

            // is the video stream available?
            if (capture.isOpened()) {
                isNotCameraActive = false;

                // grab a frame every 33 ms (30 frames/sec)
                FrameGrabberRunnable frameGrabber = new FrameGrabberRunnable(capture, currentFrame);
                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                cameraButton.setText("Stop Camera");
            } else {
                logger.error("Impossible to open the camera connection...");
            }
        } else {
            isNotCameraActive = true;
            cameraButton.setText("Start Camera");

            // stop the timer
            try {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                logger.error("Exception in stopping the frame capture, trying to release the camera now... ", e);
            }

            capture.release();
            currentFrame.setImage(null);
        }
    }

}