package io.alexeychurchill.dplayer.media.data

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.DataFetcher.DataCallback
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import java.nio.ByteBuffer
import javax.inject.Inject
import javax.inject.Provider

class CoverArtModelLoaderFactory @Inject constructor(
    private val loaderProvider: Provider<CoverArtModelLoader>,
) : ModelLoaderFactory<CoverArtPath, ByteBuffer> {

    override fun build(
        multiFactory: MultiModelLoaderFactory,
    ): ModelLoader<CoverArtPath, ByteBuffer> {
        return loaderProvider.get()
    }

    override fun teardown() {
        /** NO OP **/
    }
}

class CoverArtModelLoader @Inject constructor(
    private val dataFetcherFactory: CoverArtDataFetcher.Factory,
) : ModelLoader<CoverArtPath, ByteBuffer> {

    override fun buildLoadData(
        model: CoverArtPath,
        width: Int,
        height: Int,
        options: Options,
    ): LoadData<ByteBuffer>? {
        if (model !is CoverArtPath.LocalUri) {
            return null
        }
        val key = ObjectKey(model.mediaUri) // TODO: Implement custom Key class
        val dataFetcher = dataFetcherFactory.create(model)
        return LoadData(key, dataFetcher)
    }

    override fun handles(model: CoverArtPath): Boolean {
        return model is CoverArtPath.LocalUri
    }
}

class CoverArtDataFetcher @AssistedInject constructor(
    @Assisted private val coverArtPath: CoverArtPath.LocalUri,
    private val metadataResolver: MediaMetadataResolver,
) : DataFetcher<ByteBuffer> {

    override fun loadData(
        priority: Priority,
        callback: DataCallback<in ByteBuffer>,
    ) {
        val bytes = metadataResolver.getCoverArtBytes(coverArtPath.mediaUri)
        callback.onDataReady(bytes?.let { ByteBuffer.allocate(it.size).apply { put(it) } })
    }

    override fun cleanup() {
        /** NO OP **/
    }

    override fun cancel() {
        /** NO OP **/
    }

    override fun getDataClass(): Class<ByteBuffer> = ByteBuffer::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL

    @AssistedFactory
    interface Factory {
        fun create(@Assisted coverArtPath: CoverArtPath.LocalUri): CoverArtDataFetcher
    }
}
