/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

/**
 * Expands a file that has been compressed with the GZIP
 * algorithm. Normally used to compress non-compressed archives such
 * as TAR files.
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */

public class GUnzip extends Unpack {
    private static final int BUFFER_SIZE = 8 * 1024;
    private static final String DEFAULT_EXTENSION = ".gz";

    /**
     * Get the default extension.
     * @return the value ".gz"
     */
    protected String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    /**
     * Implement the gunzipping.
     */
    protected void extract() {
        if (source.lastModified() > dest.lastModified()) {
            log("Expanding " + source.getAbsolutePath() + " to "
                        + dest.getAbsolutePath());

            FileOutputStream out = null;
            GZIPInputStream zIn = null;
            InputStream fis = null;
            try {
                out = new FileOutputStream(dest);
                fis = srcResource.getInputStream();
                zIn = new GZIPInputStream(fis);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                do {
                    out.write(buffer, 0, count);
                    count = zIn.read(buffer, 0, buffer.length);
                } while (count != -1);
            } catch (IOException ioe) {
                String msg = "Problem expanding gzip " + ioe.getMessage();
                throw new BuildException(msg, ioe, getLocation());
            } finally {
                FileUtils.close(fis);
                FileUtils.close(out);
                FileUtils.close(zIn);
            }
        }
    }

    /**
     * Whether this task can deal with non-file resources.
     *
     * <p>This implementation returns true only if this task is
     * &lt;gunzip&gt;.  Any subclass of this class that also wants to
     * support non-file resources needs to override this method.  We
     * need to do so for backwards compatibility reasons since we
     * can't expect subclasses to support resources.</p>
     * @return true if this task supports non file resources.
     * @since Ant 1.7
     */
    protected boolean supportsNonFileResources() {
        return getClass().equals(GUnzip.class);
    }
}
