# MyCameraDemo
	Just a test about the use of Android Camera.

## 1、 start camera
	start camera through the folloing action:
	android.media.action.IMAGE_CAPTURE

## 2、 runtime permission
	About the storage permission, need to do with the runtime permission.
	"android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.WRITE_EXTERNAL_STORAGE"

## 3、 file:// URI and FileProvider
	from Android N, the file:// URI is not been used, it will throw FileUriExposedException and app will crash. Pls use FileProvider instead of it.

## 4 、Custom Camera
	use android.hardware.Camera to preview.
	The preview View will use SurfaceView, because the real-time view frequency is 60fps.

## 5、 Take picture
	Use mCamera.takePicture to capture, do not forget to focus when take picture.
	Need to do with store picture after take picture, through the onPictureTaken function of Camera.PictureCallback.

## 6 、 Watermark
	Add the watermark when preview, but did not write the watermark to the photo when store.
	Pls think it when in idle.