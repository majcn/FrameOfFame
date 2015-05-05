package si.majcn.frameoffame.facecropper;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.FaceDetector;

import cat.lafosca.facecropper.BitmapUtils;

public class FaceCropper extends cat.lafosca.facecropper.FaceCropper {

    protected CropResult[] cropFaces(Bitmap original) {
        Bitmap fixedBitmap = BitmapUtils.forceEvenBitmapSize(original);
        fixedBitmap = BitmapUtils.forceConfig565(fixedBitmap);
        Bitmap mutableBitmap = fixedBitmap.copy(Bitmap.Config.RGB_565, true);

        if (fixedBitmap != mutableBitmap) {
            fixedBitmap.recycle();
        }

        int maxFaces = getMaxFaces();
        FaceDetector faceDetector = new FaceDetector(mutableBitmap.getWidth(), mutableBitmap.getHeight(), maxFaces);
        FaceDetector.Face[] faces = new FaceDetector.Face[maxFaces];
        int faceCount = faceDetector.findFaces(mutableBitmap, faces);

        CropResult[] results = new CropResult[faceCount + 1];
        results[0] = new CropResult(mutableBitmap);

        PointF centerFace = new PointF();
        for (int i = 0; i < faceCount; i++) {
            FaceDetector.Face face = faces[i];

            // Eyes distance * 3 usually fits an entire face
            int faceSize = (int) (face.eyesDistance() * 3);

            switch (getSizeMode()) {
                case FaceMarginPx:
                    faceSize += getFaceMarginPx() * 2; // *2 for top and down/right and left effect
                    break;
                case EyeDistanceFactorMargin:
                    faceSize += face.eyesDistance() * getEyeDistanceFactorMargin();
                    break;
                default:
                    break;
            }

            faceSize = Math.max(faceSize, getFaceMinSize());

            face.getMidPoint(centerFace);

            int tInitX = (int) (centerFace.x - faceSize / 2);
            int tInitY = (int) (centerFace.y - faceSize / 2);
            tInitX = Math.max(0, tInitX);
            tInitY = Math.max(0, tInitY);

            int tEndX = tInitX + faceSize;
            int tEndY = tInitY + faceSize;
            tEndX = Math.min(tEndX, mutableBitmap.getWidth());
            tEndY = Math.min(tEndY, mutableBitmap.getHeight());

            int sizeX = tEndX - tInitX;
            int sizeY = tEndY - tInitY;

            if (sizeX + tInitX > mutableBitmap.getWidth()) {
                sizeX = mutableBitmap.getWidth() - tInitX;
            }

            if (sizeY + tInitY > mutableBitmap.getHeight()) {
                sizeY = mutableBitmap.getHeight() - tInitY;
            }

            int size = Math.max(sizeX, sizeY);
            size = Math.min(mutableBitmap.getHeight(), size);
            size = Math.min(mutableBitmap.getWidth(), size);

            if (size + tInitX > mutableBitmap.getWidth()) {
                tInitX = mutableBitmap.getWidth() - size - tInitX;
            }
            if (size + tInitY > mutableBitmap.getHeight()) {
                tInitY = mutableBitmap.getHeight() - size - tInitY;
            }

            Point init = new Point(tInitX, tInitY);
            Point end = new Point(tInitX + size, tInitY + size);
            results[i + 1] = new CropResult(mutableBitmap, init, end);
        }

        return results;
    }

    public Bitmap[] getCroppedImages(Bitmap bitmap) {
        CropResult[] results = cropFaces(bitmap);

        boolean canRecycle = true;

        Bitmap[] croppedImages = new Bitmap[results.length];
        for (int i=0; i<results.length; i++) {
            CropResult result = results[i];

            croppedImages[i] = Bitmap.createBitmap(result.getBitmap(),
                result.getInit().x,
                result.getInit().y,
                result.getEnd().x - result.getInit().x,
                result.getEnd().y - result.getInit().y);

            if (result.getBitmap() == croppedImages[i]) {
                canRecycle = false;
            }
        }

        // We know that all CropResult's hold same instance of image, so we only need to recycle one
        if (canRecycle && results.length > 0) {
            results[0].getBitmap().recycle();
        }

        return croppedImages;
    }
}
