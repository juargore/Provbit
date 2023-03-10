@file:Suppress("VARIABLE_IN_SINGLETON_WITHOUT_THREAD_LOCAL")
package ai.bright.provbit.util

import ai.bright.provbit.demo.entities.ImageAnalysis
import kotlin.collections.List

object PrePostProcessor {

    /**
     * for yolov5 model, no need to apply MEAN and STD
     */
    var NO_MEAN_RGB = floatArrayOf(0.0f, 0.0f, 0.0f)
    var NO_STD_RGB = floatArrayOf(1.0f, 1.0f, 1.0f)

    /**
     * Store the N classes that exists on file classes.txt on Assets.
     */
    var mClasses: List<String?> = listOf()

    /**
     * Model output is of size 25200*(num_of_class+5)
     */
    private const val mOutputRow = 25200 // as decided by the YOLOv5 model for input image of size 640*640
    private const val mOutputColumn = 85 // left, top, right, bottom, score and 80 class probability
    private const val mThreshold = 0.30f // score above which a detection is generated
    private const val mNmsLimit = 15

    /**
     * Removes bounding boxes that overlap too much with other boxes that have
     * a higher score.
     * - Parameters:
     * - boxes: an array of bounding boxes and their scores
     * - limit: the maximum number of boxes that will be selected
     * - threshold: used to decide whether boxes overlap too much
     */
    private fun nonMaxSuppression(
        boxes: MutableList<ImageAnalysis>,
        limit: Int
    ): ArrayList<ImageAnalysis> {

        val selected: MutableList<ImageAnalysis> = ArrayList<ImageAnalysis>()
        val active = BooleanArray(boxes.size).map { true }

        /**
         * How the algorithm works: Start with the box that has the highest score.
         * Remove any remaining boxes that overlap it more than the given threshold
         * amount. If there are any boxes left (i.e. these did not overlap with any
         * previous boxes), then repeat this procedure, until no more boxes remain
         * or the limit has been reached.
         */

        var i = 0
        while (i < boxes.size) {
            if (active[i]) {
                val boxA: ImageAnalysis = boxes[i]
                selected.add(boxA)
                if (selected.size >= limit) break
            }
            i++
        }
        return selected as ArrayList<ImageAnalysis>
    }

    fun outputsToNMSPredictions(outputs: FloatArray): ArrayList<ImageAnalysis> {
        val results: MutableList<ImageAnalysis> = ArrayList<ImageAnalysis>()

        for (i in 0 until mOutputRow) {
            if (outputs[i * mOutputColumn + 4] > mThreshold) {
                var max = outputs[i * mOutputColumn + 5]
                var cls = 0
                for (j in 0 until mOutputColumn - 5) {
                    if (outputs[i * mOutputColumn + 5 + j] > max) {
                        max = outputs[i * mOutputColumn + 5 + j]
                        cls = j
                    }
                }

                val resultFromImage = ImageAnalysis(cls, outputs[i * mOutputColumn + 4])
                results.add(resultFromImage)
            }
        }

        return nonMaxSuppression(results, mNmsLimit)
    }
}
