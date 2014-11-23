/* GIMP - The GNU Image Manipulation Program
 * Copyright (C) 1995 Spencer Kimball and Peter Mattis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include "color-balance.h"
#include "macros.h"
#include "gimpcolorspace.h"


/*  local function prototypes  */

static void color_balance_transfer_init (void);


/*  private variables  */

static int transfer_initialized = 0;

static double shadows[256] = { 0 };


/*  public functions  */

void
color_balance_init (ColorBalance *cb,
                    double        c_r,
                    double        m_g,
                    double        y_b)
{
    cb->cyan_red = c_r;
    cb->magenta_green = m_g;
    cb->yellow_blue = y_b;
}

void
color_balance_setup (ColorBalance *cb)
{
    int i;
    signed int r_n, g_n, b_n;

    if (transfer_initialized == 0)
    {
        color_balance_transfer_init ();
        transfer_initialized = 1;
    }

    for (i = 0; i < 256; i++)
    {
        r_n = i;
        g_n = i;
        b_n = i;

        r_n += cb->cyan_red * shadows[i];
        r_n = CLAMP0255 (r_n);

        g_n += cb->magenta_green * shadows[i];
        g_n = CLAMP0255 (g_n);

        b_n += cb->yellow_blue * shadows[i];
        b_n = CLAMP0255 (b_n);

        cb->r_lookup[i] = r_n;
        cb->g_lookup[i] = g_n;
        cb->b_lookup[i] = b_n;
    }
}

void
color_balance_process (ColorBalance      *cb,
                       AndroidBitmapInfo *info,
                       void              *pixels)
{
    int xx, yy, r, g, b, r_n, g_n, b_n;
    uint32_t *line;

    for (yy = 0; yy < info->height; yy++)
    {
        line = (uint32_t *)pixels;
        for (xx = 0; xx < info->width; xx++)
        {
            b = (int) ((line[xx] & 0x00FF0000) >> 16);
            g = (int) ((line[xx] & 0x0000FF00) >> 8);
            r = (int) (line[xx] & 0x00000FF );

            r_n = cb->r_lookup[r];
            g_n = cb->g_lookup[g];
            b_n = cb->b_lookup[b];

            gimp_rgb_to_hsl_int (&r_n, &g_n, &b_n);
            b_n = gimp_rgb_to_l_int (r, g, b);
            gimp_hsl_to_rgb_int (&r_n, &g_n, &b_n);

            line[xx] = ((255 << 24) & 0xFF000000) | ((b_n << 16) & 0x00FF0000) | ((g_n << 8) & 0x0000FF00) | (r_n & 0x000000FF);
        }
        pixels = (char *)pixels + info->stride;
    }
}


/*  private functions  */

static void
color_balance_transfer_init (void)
{
    int i;
    static const double a = 64, b = 85, scale = 1.785;

    for (i = 0; i < 256; i++)
    {
        double low = CLAMP ((i - b) / -a + .5, 0, 1) * scale;

        shadows[i] = low;
    }
}
