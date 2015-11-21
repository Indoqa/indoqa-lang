/*
 * Licensed to the Indoqa Software Design und Beratung GmbH (Indoqa) under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Indoqa licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoqa.lang.geo;

import java.awt.geom.Point2D;

/**
 * Common geometric functions.
 */
public final class GeometryHelper {

    private static final double EARTH_RADIUS_IN_METERS = 6371000.8;

    private static final int MINUTES_PER_DEGREE = 60;
    private static final double LAND_MILES_PER_NAUTICAL_MILE = 1.1515;
    private static final double KILOMETERS_PER_LAND_MILE = 1.609344;

    private GeometryHelper() {
        // hide utility class constructor
    }

    /**
     * Returns a simple 2D projection of the given geo coordinates.<br>
     * <br>
     * This will not try to correct for the curvature of the earth!
     * 
     * @param lat The latitude
     * @param lon The longitude
     * 
     * @return The Point2D containing 2D coordinates in meters.
     */
    public static Point2D getCoordinates2D(double lat, double lon) {
        double x = Math.toRadians(lon);
        double y = Math.toRadians(lat);

        return new Point2D.Double(x * EARTH_RADIUS_IN_METERS, y * EARTH_RADIUS_IN_METERS);
    }

    /**
     * The distance in kilometers between two geo coordinates.
     * 
     * @param lat1 Latitude of the first point.
     * @param lon1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lon2 Longitude of the first point.
     * 
     * @return The distance between the two points in kilometer.
     */
    public static double getGeoDistance(double lat1, double lon1, double lat2, double lon2) {
        if (lat1 == lat2 && lon1 == lon2) {
            return 0;
        }

        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lon1 - lon2));
        dist = Math.min(1, dist);
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * MINUTES_PER_DEGREE * LAND_MILES_PER_NAUTICAL_MILE * KILOMETERS_PER_LAND_MILE;

        return dist;
    }

    /**
     * Mercator projection of the given geo coordinates.
     */
    public static Point2D getMercatorProjection(double lat, double lon, double lonCenter) {
        double x = Math.toRadians(lon) - Math.toRadians(lonCenter);

        double sinusLat = Math.sin(Math.toRadians(lat));
        double y = 0.5 * Math.log((1 + sinusLat) / (1 - sinusLat));

        return new Point2D.Double(x * EARTH_RADIUS_IN_METERS, y * EARTH_RADIUS_IN_METERS);
    }

    /**
     * Squared distance between the given point and the line from <code>start</code> to <code>end</code>.<br>
     */
    public static double getSquaredDistance(Point2D point, double startX, double startY, double endX, double endY) {
        double vectorX = endX - startX;
        double vectorY = endY - startY;
        double pointX = point.getX();
        double pointY = point.getY();

        if (vectorX == 0 && vectorY == 0) {
            return Point2D.distanceSq(pointX, pointY, startX, startY);
        }

        double lambda = (vectorX * (pointX - startX) + vectorY * (pointY - startY)) / (vectorX * vectorX + vectorY * vectorY);

        if (lambda < 0) {
            return Point2D.distanceSq(pointX, pointY, startX, startY);
        }

        if (lambda > 1) {
            return Point2D.distanceSq(pointX, pointY, endX, endY);
        }

        double intersectionX = startX + lambda * vectorX;
        double intersectionY = startY + lambda * vectorY;
        return (intersectionX - pointX) * (intersectionX - pointX) + (intersectionY - pointY) * (intersectionY - pointY);
    }

    /**
     * Squared distance between the given point and the line from <code>start</code> to <code>end</code>.<br>
     */
    public static double getSquaredDistance(Point2D point, Point2D start, Point2D end) {
        return getSquaredDistance(point, start.getX(), start.getY(), end.getX(), end.getY());
    }
}
