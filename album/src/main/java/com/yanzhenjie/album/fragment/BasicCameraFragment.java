/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.github.privacystreams.commons.item.ItemOperators;
import com.github.privacystreams.core.*;
import com.github.privacystreams.core.Callback;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.image.Image;
import com.github.privacystreams.image.ImageOperators;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.fragment.NoFragment;

import java.io.File;

/**
 * <p>Fragment with camera function.</p>
 * Created by yanzhenjie on 17-3-29.
 */
abstract class BasicCameraFragment extends NoFragment {

    private static final String INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH";
    private String mCameraFilePath;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath);
        super.onSaveInstanceState(outState);
    }

    /**
     * Camera, but unknown permissions.
     */
    protected void cameraUnKnowPermission() {
        UQI uqi = new UQI(this.getContext());
        uqi.getData(Image.takeFromCamera(), Purpose.UTILITY("taking picture."))
                .setField("imagePath", ImageOperators.getFilepath(Image.IMAGE_DATA))
                .ifPresent("imagePath", new Callback<String>() {
                    @Override
                    protected void onInput(String imagePath) {
                        onCameraBack(imagePath);
                    }
                    @Override
                    protected void onFail(PSException exception) {
                        AlertDialog.build(getContext())
                                .setTitle(R.string.album_dialog_permission_failed)
                                .setMessage(R.string.album_permission_camera_failed_hint)
                                .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Nothing.
                                    }
                                })
                                .show();
                    }
                });
    }

    /**
     * After the camera is finished.
     *
     * @param imagePath file path.
     */
    protected abstract void onCameraBack(String imagePath);

}
