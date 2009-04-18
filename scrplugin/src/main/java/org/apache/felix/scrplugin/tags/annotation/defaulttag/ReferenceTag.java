/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scrplugin.tags.annotation.defaulttag;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.scrplugin.Constants;
import org.apache.felix.scrplugin.tags.JavaClassDescription;
import org.apache.felix.scrplugin.tags.JavaField;

import com.thoughtworks.qdox.model.Annotation;

/**
 * Description of a java tag for components.
 */
public class ReferenceTag extends AbstractTag {

    protected final Reference annotation;

    /**
     * @param annotation Annotation
     * @param desc Description
     * @param field Field
     */
    public ReferenceTag(Reference annotation, JavaClassDescription desc, JavaField field) {
        super(desc, field);
        this.annotation = annotation;
    }

    /**
     * @param annotation Annotation
     * @param desc Description
     */
    public ReferenceTag(final Annotation annotation, JavaClassDescription desc, JavaField field) {
        super(desc, field);

        this.annotation = new Reference() {

            public String bind() {
                return Util.getStringValue(annotation, "bind", Reference.class);
            }

            public ReferenceCardinality cardinality() {
                final Object obj = annotation.getNamedParameter("cardinality");
                if ( obj != null ) {
                    if ( obj instanceof ReferenceCardinality ) {
                        return (ReferenceCardinality)obj;
                    }
                    return ReferenceCardinality.values()[(Integer)obj];
                }
                try {
                    return (ReferenceCardinality) Reference.class.getMethod("cardinality").getDefaultValue();
                } catch( NoSuchMethodException mnfe) {
                    // we ignore this
                    return null;
                }
            }

            public boolean checked() {
                return Util.getBooleanValue(annotation, "checked", Reference.class);
            }

            public String name() {
                return Util.getStringValue(annotation, "name", Reference.class);
            }

            public ReferencePolicy policy() {
                final Object obj = annotation.getNamedParameter("policy");
                if ( obj != null ) {
                    if ( obj instanceof ReferencePolicy ) {
                        return (ReferencePolicy)obj;
                    }
                    return ReferencePolicy.values()[(Integer)obj];
                }
                try {
                    return (ReferencePolicy) Reference.class.getMethod("policy").getDefaultValue();
                } catch( NoSuchMethodException mnfe) {
                    // we ignore this
                    return null;
                }
            }

            public Class<?> referenceInterface() {
                return Util.getClassValue(annotation, "referenceInterface", Reference.class);
            }

            public String strategy() {
                return Util.getStringValue(annotation, "strategy", Reference.class);
            }

            public String target() {
                return Util.getStringValue(annotation, "target", Reference.class);
            }

            public String unbind() {
                return Util.getStringValue(annotation, "unbind", Reference.class);
            }

            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return null;
            }
        };
    }

    @Override
    public String getName() {
        return Constants.REFERENCE;
    }

    @Override
    public Map<String, String> getNamedParameterMap() {
        final Map<String, String> map = new HashMap<String, String>();

        map.put(Constants.REFERENCE_NAME, emptyToNull(this.annotation.name()));

        if (this.annotation.referenceInterface() != AutoDetect.class) {
            String referenceInterface = this.annotation.referenceInterface().getName();
            map.put(Constants.REFERENCE_INTERFACE, referenceInterface);
        }

        map.put(Constants.REFERENCE_CARDINALITY, this.annotation.cardinality().getCardinalityString());
        map.put(Constants.REFERENCE_POLICY, this.annotation.policy().getPolicyString());
        map.put(Constants.REFERENCE_TARGET, emptyToNull(this.annotation.target()));
        map.put(Constants.REFERENCE_BIND, emptyToNull(this.annotation.bind()));
        map.put(Constants.REFERENCE_UNDBIND, emptyToNull(this.annotation.unbind()));
        map.put(Constants.REFERENCE_CHECKED, String.valueOf(this.annotation.checked()));
        map.put(Constants.REFERENCE_STRATEGY, emptyToNull(this.annotation.strategy()));

        return map;
    }

}
