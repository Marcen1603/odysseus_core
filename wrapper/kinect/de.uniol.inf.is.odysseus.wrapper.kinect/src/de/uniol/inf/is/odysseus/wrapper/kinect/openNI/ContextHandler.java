package de.uniol.inf.is.odysseus.wrapper.kinect.openNI;

import java.util.ArrayList;
import java.util.HashMap;

import org.openni.CalibrationProgressEventArgs;
import org.openni.CalibrationProgressStatus;
import org.openni.Context;
import org.openni.DepthGenerator;
import org.openni.FieldOfView;
import org.openni.GeneralException;
import org.openni.IObservable;
import org.openni.IObserver;
import org.openni.ImageGenerator;
import org.openni.ImageMetaData;
import org.openni.Point3D;
import org.openni.PoseDetectionCapability;
import org.openni.PoseDetectionEventArgs;
import org.openni.SkeletonCapability;
import org.openni.SkeletonJoint;
import org.openni.SkeletonJointPosition;
import org.openni.SkeletonProfile;
import org.openni.StatusException;
import org.openni.UserEventArgs;
import org.openni.UserGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The context handler handles the connection via USB to the Kinect camera and stores all
 * of the needed information.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public final class ContextHandler {
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(ContextHandler.class);
    
    /** Horizontal field of view of the depth camera. */
    private float fovH;

    /** Vertical field of view of the depth camera. */
    private float fovV;

    /** Context of the Kinect camera. */
    private Context context;

    /** Stores the depth generator. */
    private DepthGenerator depthGen;

    /** Stores the image generator. */
    private ImageGenerator imageGen;
    
    /** Stores the user generator. */
    private UserGenerator userGen;
    
    /** Stores the skeleton capability. */
    private SkeletonCapability skeletonCap;
    
    /** Stores the pose detection capability. */
    private PoseDetectionCapability poseDetectionCap;
    
    /** Stores the calibration pose string. */
    String calibPose = null;
    
    /** HashMap that holds all the skeletons. */
    HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> joints;

    /** Stores the image widht. */
    private int width;

    /** Stores the image height. */
    private int height;

    /** Singleton instance reference. */
    private static ContextHandler instance = null;

    /** Thread to receive updates. */
    private UpdateThread updateThread;

    /** List of update listeners. */
    private ArrayList<DataReceivedListener> listener = new ArrayList<>();

    /**
     * Singleton constructor, will be called from {@link #getInstance()}.
     */
    private ContextHandler() {
        try {
            context = new Context();

            depthGen = DepthGenerator.create(context);
            imageGen = ImageGenerator.create(context);
            ImageMetaData imageMD = imageGen.getMetaData();
            width = imageMD.getFullXRes();
            height = imageMD.getFullYRes();

            FieldOfView fov = depthGen.getFieldOfView();
            fovH = (float) fov.getHFOV();
            fovV = (float) fov.getVFOV();
            
            userGen = UserGenerator.create(context);
            skeletonCap = userGen.getSkeletonCapability();
            poseDetectionCap = userGen.getPoseDetectionCapability();
            
            userGen.getNewUserEvent().addObserver(new NewUserObserver());
            userGen.getLostUserEvent().addObserver(new LostUserObserver());
            skeletonCap.getCalibrationCompleteEvent().addObserver(new CalibrationCompleteObserver());
            poseDetectionCap.getPoseDetectedEvent().addObserver(new PoseDetectedObserver());
            
            calibPose = skeletonCap.getSkeletonCalibrationPose();
            joints = new HashMap<Integer, HashMap<SkeletonJoint,SkeletonJointPosition>>();
            
            skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the context handler.
     * @return Instance of {@link ContextHandler}.
     */
    public static ContextHandler getInstance() {
        if (instance == null) {
            instance = new ContextHandler();
        }
        return instance;
    }

    /**
     * Adds a {@link DataReceivedListener} to be notified on updates. The first listener
     * starts the update thread.
     * @param l
     * Listener to add.
     */
    public void addDataReceivedListener(DataReceivedListener l) {
        synchronized (listener) {
            if (listener.contains(l)) {
                throw new RuntimeException(
                        "The passed listener is already contained in the array.");
            }

            listener.add(l);
            if (listener.size() == 1) {
                // First listener has been added -> start the thread
                updateThread = new UpdateThread();
                updateThread.start();
            }
        }
    }

    /**
     * Removes a {@link DataReceivedListener} from the notification updates. The last
     * removed listener ends the update thread.
     * @param l
     * Listener to be removed
     */
    public void removeDataReceivedListener(DataReceivedListener l) {
        synchronized (listener) {
            if (!listener.contains(l)) {
                throw new RuntimeException(
                        "The passed listener is not contained in the array.");
            }

            listener.remove(l);
            if (listener.size() == 0) {
                // Last listener has been removed -> end the thread
                updateThread.shutDown();
            }
        }
    }

    /**
     * Gets the context of the Kinect camera.
     * @return The Kinect {@link Context}
     */
    public Context getContext() {
        return context;
    }

    /**
     * Gets the image generator. It holds the image map after each frame.
     * @return {@link ImageGenerator}.
     */
    public ImageGenerator getImageGenerator() {
        return imageGen;
    }

    /**
     * Gets the depth generator. It holds the depth map after each frame.
     * @return {@link DepthGenerator}.
     */
    public DepthGenerator getDepthGenerator() {
        return depthGen;
    }

    /**
     * Gets the width of a Kinect image.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of a Kinect image.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Blocks the current thread until the Kinect camera has updated all of it contents.
     */
    public void waitForUpdate() {
        try {
            context.startGeneratingAll();
            context.waitAnyUpdateAll();
        } catch (StatusException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the horizontal field of view of the depth camera.
     * @return Horizontal field of view.
     */
    public float getFovH() {
        return fovH;
    }

    /**
     * Gets the vertical field of view of the depth camera.
     * @return Vertical field of view.
     */
    public float getFovV() {
        return fovV;
    }
    
    private void calculateJoint(int user, SkeletonJoint joint) throws StatusException
    {
        SkeletonJointPosition pos = skeletonCap.getSkeletonJointPosition(user, joint);
        if (pos.getPosition().getZ() != 0)
        {
            joints.get(user).put(joint, new SkeletonJointPosition(pos.getPosition(), pos.getConfidence()));
        }
        else
        {
            joints.get(user).put(joint, new SkeletonJointPosition(new Point3D(), 0));
        }
    }
    
    private void calculateJoints(int user) throws StatusException
    {
        calculateJoint(user, SkeletonJoint.HEAD);
        calculateJoint(user, SkeletonJoint.NECK);
        
        calculateJoint(user, SkeletonJoint.LEFT_SHOULDER);
        calculateJoint(user, SkeletonJoint.LEFT_ELBOW);
        calculateJoint(user, SkeletonJoint.LEFT_HAND);

        calculateJoint(user, SkeletonJoint.RIGHT_SHOULDER);
        calculateJoint(user, SkeletonJoint.RIGHT_ELBOW);
        calculateJoint(user, SkeletonJoint.RIGHT_HAND);

        calculateJoint(user, SkeletonJoint.TORSO);

        calculateJoint(user, SkeletonJoint.LEFT_HIP);
        calculateJoint(user, SkeletonJoint.LEFT_KNEE);
        calculateJoint(user, SkeletonJoint.LEFT_FOOT);

        calculateJoint(user, SkeletonJoint.RIGHT_HIP);
        calculateJoint(user, SkeletonJoint.RIGHT_KNEE);
        calculateJoint(user, SkeletonJoint.RIGHT_FOOT);
    }
    
    public HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> getJoints() {
        int[] users;
        try {
            users = userGen.getUsers();
            for (int i = 0; i < users.length; ++i)
            {
                if (skeletonCap.isSkeletonTracking(users[i])) {
                    calculateJoints(users[i]);
                }
            }
        } catch (StatusException e) {
            e.printStackTrace();
            return null;
        }
        return joints;
    }

    /**
     * Worker thread, that blocking waits for a Kinect update and notifies all of the
     * registered {@link DataReceivedListener} in the {@link ContextHandler}.
     * @author Juergen Boger <juergen.boger@offis.de>
     */
    class UpdateThread extends Thread {
        /** Is the thread active. */
        private boolean running = true;

        /**
         * Shutdown this thread.
         */
        public void shutDown() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                waitForUpdate();
                synchronized (listener) {
                    for (DataReceivedListener l : listener) {
                        l.onDataReceived();
                    }
                }
            }
        }
    }
    
    class NewUserObserver implements IObserver<UserEventArgs>
    {
        @Override
        public void update(IObservable<UserEventArgs> observable,
                UserEventArgs args)
        {
            log.debug("New user " + args.getId());
            try
            {
                if (skeletonCap.needPoseForCalibration())
                {
                    poseDetectionCap.startPoseDetection(calibPose, args.getId());
                }
                else
                {
                    skeletonCap.requestSkeletonCalibration(args.getId(), true);
                }
            } catch (StatusException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    class LostUserObserver implements IObserver<UserEventArgs>
    {
        @Override
        public void update(IObservable<UserEventArgs> observable,
                UserEventArgs args)
        {
            log.debug("Lost user " + args.getId());
            joints.remove(args.getId());
        }
    }
    
    class CalibrationCompleteObserver implements IObserver<CalibrationProgressEventArgs>
    {
        @Override
        public void update(IObservable<CalibrationProgressEventArgs> observable,
                CalibrationProgressEventArgs args)
        {
            log.debug("Calibraion complete: " + args.getStatus());
            try
            {
            if (args.getStatus() == CalibrationProgressStatus.OK)
            {
                log.debug("starting tracking "  +args.getUser());
                    skeletonCap.startTracking(args.getUser());
                    joints.put(new Integer(args.getUser()), new HashMap<SkeletonJoint, SkeletonJointPosition>());
            }
            else if (args.getStatus() != CalibrationProgressStatus.MANUAL_ABORT)
            {
                if (skeletonCap.needPoseForCalibration())
                {
                    poseDetectionCap.startPoseDetection(calibPose, args.getUser());
                }
                else
                {
                    skeletonCap.requestSkeletonCalibration(args.getUser(), true);
                }
            }
            } catch (StatusException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    class PoseDetectedObserver implements IObserver<PoseDetectionEventArgs>
    {
        @Override
        public void update(IObservable<PoseDetectionEventArgs> observable,
                PoseDetectionEventArgs args)
        {
            log.debug("Pose " + args.getPose() + " detected for " + args.getUser());
            try
            {
                poseDetectionCap.stopPoseDetection(args.getUser());
                skeletonCap.requestSkeletonCalibration(args.getUser(), true);
            } catch (StatusException e)
            {
                e.printStackTrace();
            }
        }
    }
}
