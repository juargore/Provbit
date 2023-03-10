package ai.bright.provbit.android.demo.adapters.auth0.ports

import ai.bright.provbit.demo.domain.ports.ImageRecognitionRepository
import ai.bright.provbit.demo.entities.ImageAnalysis
import ai.bright.provbit.util.PrePostProcessor
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ImgRecognitionRepository @Inject constructor(
    private val app: Application
) : ImageRecognitionRepository {

    override suspend fun analyzeImage(image: ByteArray): List<ImageAnalysis> {
        return getResultsFromPytorchLibrary(byteArrayToBitmap(image))
    }

    private fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun getResultsFromPytorchLibrary(mBitmap: Bitmap): ArrayList<ImageAnalysis> {
        val resizedBitmap = Bitmap.createScaledBitmap(
            mBitmap,
            640,
            640,
            true
        )
        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            resizedBitmap,
            PrePostProcessor.NO_MEAN_RGB,
            PrePostProcessor.NO_STD_RGB
        )
        val mModule = LiteModuleLoader.load(assetFilePath("yolov5s.torchscript.ptl"))

        /**
         * Read the classes.txt file and add every line (is a single class) to a mutableList,
         */
        try {
            val inputString = app.assets.open("classes.txt").bufferedReader().use { it.readText() }
            val classes: List<String> = inputString.split("\n")
            PrePostProcessor.mClasses = classes
        } catch (e: Exception) {
            Log.e("--", "Error reading assets: ${e.localizedMessage}")
        }

        val outputTuple: Array<IValue> = mModule.forward(IValue.from(inputTensor)).toTuple()
        val outputTensor = outputTuple[0].toTensor()
        val outputs = outputTensor.dataAsFloatArray

        return PrePostProcessor.outputsToNMSPredictions(outputs)
    }

    @Throws(IOException::class)
    fun assetFilePath(assetName: String?): String? {
        val file = File(app.filesDir, assetName!!)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        app.assets.open(assetName).use { inStr ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inStr.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }
}
