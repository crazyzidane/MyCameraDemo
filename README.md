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