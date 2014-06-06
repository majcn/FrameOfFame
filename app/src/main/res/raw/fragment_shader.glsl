precision lowp float;

uniform mediump sampler2D tex_sampler;
varying mediump vec2 v_texcoord;

uniform lowp int filter_index;

vec4 getQ(vec3 RGB)
{
    vec4 P = (RGB.g < RGB.b) ? vec4(RGB.bg, -1.0, 2.0 / 3.0) : vec4(RGB.gb, 0.0, -1.0 / 3.0);
    return (RGB.r < P.x) ? vec4(P.xyw, RGB.r) : vec4(RGB.r, P.yzx);
}

float RGBtoL(vec3 RGB)
{
    vec4 Q = getQ(RGB);
    return (Q.x + min(Q.w, Q.y)) * 0.5;
}

vec3 RGBtoHSL(vec3 RGB)
{
    vec4 Q = getQ(RGB);
    float C = Q.x - min(Q.w, Q.y);

    float H = abs((Q.w - Q.y) / (6.0 * C + 1e-10) + Q.z);
    float L = Q.x - C * 0.5;
    float S = C / (1.0 - abs(L * 2.0 - 1.0) + 1e-10);

    return vec3(H, S, L);
}

vec3 HSLtoRGB(vec3 HSL)
{
    float C = (1.0 - abs(2.0 * HSL.z - 1.0)) * HSL.y;

    float R = abs(HSL.x * 6.0 - 3.0) - 1.0;
    float G = 2.0 - abs(HSL.x * 6.0 - 2.0);
    float B = 2.0 - abs(HSL.x * 6.0 - 4.0);

    return (clamp(vec3(R, G, B), 0.0, 1.0) - 0.5) * C + HSL.z;
}

vec3 color_balance(vec3 RGB, vec3 shift)
{
    float L = RGBtoL(RGB);

    const float a = 0.25;
    const float b = 0.333;
    const float scale = 0.7;
    float shadow = clamp((L - b) / -a + 0.5, 0.0, 1.0) * scale;
    // float midtones = clamp((L - b) /  a + 0.5, 0.0, 1.0) * clamp ((L + b - 1.0) / -a + 0.5, 0.0, 1.0) * scale;

    vec3 RGB_SHIFTED = clamp(shift * shadow + RGB, 0.0, 1.0);
    vec3 HSL_SHIFTED = RGBtoHSL(RGB_SHIFTED);

    return HSLtoRGB(vec3(HSL_SHIFTED.xy, L));
}

vec3 posterize(vec3 RGB, float levels)
{
    return clamp(floor(RGB * (levels - 1.0) + 0.5) / (levels - 1.0), 0.0, 1.0);
}

vec3 hue_saturation_shift(vec3 RGB, vec3 shift)
{
    vec3 HSL = RGBtoHSL(RGB);

    float H = HSL.x + shift.x;
    H = (H < 0.0) ? H + 1.0 : ((H > 1.0) ? H - 1.0 : H);

    float S = clamp(HSL.y * (shift.y * 2.0 + 1.0), 0.0, 1.0);

    float L = (shift.z < 0.0) ? HSL.z * (shift.z + 1.0) : HSL.z + (shift.z * (1.0 - HSL.z));

    return HSLtoRGB(vec3(H, S, L));
}

void main()
{
    vec4 color = texture2D(tex_sampler, v_texcoord);
    vec3 pcolor = color.rgb;
    if (filter_index == 0)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.32, 0.16));
        pcolor = posterize(pcolor, 4.0);
    }
    else if (filter_index == 1)
    {
        pcolor = color_balance(pcolor, vec3(-0.36, -0.37, 0.39));
        pcolor = posterize(pcolor, 4.0);
    }
    else if (filter_index == 2)
    {
        pcolor = posterize(pcolor, 2.0);
    }
    else if (filter_index == 3)
    {
        pcolor = color_balance(pcolor, vec3(1.00, -1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
    }
    else if (filter_index == 4)
    {
        pcolor = color_balance(pcolor, vec3(-1.00, -1.00, 1.00));
        pcolor = posterize(pcolor, 3.0);
    }
    else if (filter_index == 5)
    {
        pcolor = color_balance(pcolor, vec3(-1.00, 1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
    }
    else if (filter_index == 6)
    {
        pcolor = posterize(pcolor, 5.0);
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.15, 0.06));
    }
    else if (filter_index == 7)
    {
        pcolor = posterize(pcolor, 6.0);
        pcolor = color_balance(pcolor, vec3(-0.29, 0.40, 1.00));
    }
    else if (filter_index == 8)
    {
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.20, 0.25));
        pcolor = posterize(pcolor, 4.0);
    }
    else if (filter_index == 9)
    {
        pcolor = color_balance(pcolor, vec3(1.00, -1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.10, 0.20));
    }
    else if (filter_index == 10)
    {
        pcolor = color_balance(pcolor, vec3(0.34, -0.38, 0.24));
        pcolor = posterize(pcolor, 4.0);
        pcolor = color_balance(pcolor, vec3(-0.65, 0.00, 0.00));
    }
    else if (filter_index == 11)
    {
        pcolor = color_balance(pcolor, vec3(0.40, -0.40, 0.28));
        pcolor = posterize(pcolor, 4.0);
        pcolor = color_balance(pcolor, vec3(-1.00, -0.42, 1.00));
    }
    else if (filter_index == 12)
    {
        pcolor = color_balance(pcolor, vec3(0.45, -0.45, 0.35));
        pcolor = posterize(pcolor, 4.0);
    }
    else if (filter_index == 13)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.35, 0.20));
        pcolor = posterize(pcolor, 4.0);
    }
    else if (filter_index == 14)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.32, 0.16));
        pcolor = posterize(pcolor, 4.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 15)
    {
        pcolor = color_balance(pcolor, vec3(-0.36, -0.37, 0.39));
        pcolor = posterize(pcolor, 4.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 16)
    {
        pcolor = posterize(pcolor, 2.0);
        // nakljucnabarvadanatocka(2, 2);
    }
    else if (filter_index == 17)
    {
        pcolor = color_balance(pcolor, vec3(1.00, -1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 18)
    {
        pcolor = color_balance(pcolor, vec3(-1.00, -1.00, 1.00));
        pcolor = posterize(pcolor, 3.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 19)
    {
        pcolor = color_balance(pcolor, vec3(-1.00, 1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 20)
    {
        pcolor = posterize(pcolor, 5.0);
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.15, 0.06));
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 21)
    {
        pcolor = posterize(pcolor, 6.0);
        pcolor = color_balance(pcolor, vec3(-0.29, 0.40, 1.00));
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 22)
    {
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.20, 0.25));
        pcolor = posterize(pcolor, 4.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 23)
    {
        pcolor = color_balance(pcolor, vec3(1.00, -1.00, -1.00));
        pcolor = posterize(pcolor, 3.0);
        pcolor = hue_saturation_shift(pcolor, vec3(-0.41, -0.10, 0.20));
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 24)
    {
        pcolor = color_balance(pcolor, vec3(0.34, -0.38, 0.24));
        pcolor = posterize(pcolor, 4.0);
        pcolor = color_balance(pcolor, vec3(-0.65, 0.00, 0.00));
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 25)
    {
        pcolor = color_balance(pcolor, vec3(0.40, -0.40, 0.28));
        pcolor = posterize(pcolor, 4.0);
        pcolor = color_balance(pcolor, vec3(-1.00, -0.42, 1.00));
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 26)
    {
        pcolor = color_balance(pcolor, vec3(0.45, -0.45, 0.35));
        pcolor = posterize(pcolor, 4.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 27)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.35, 0.20));
        pcolor = posterize(pcolor, 4.0);
        // nakljucniefekt(rr, gg, bb);
    }
    else if (filter_index == 28)
    {
        pcolor = posterize(pcolor, 2.0);
        // nakljucnabarvadanatocka(3, 3);
    }
    else if (filter_index == 29)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.35, 0.20));
        pcolor = posterize(pcolor, 4.0);
        // nakljucnabarvadanatocka(3, 3);
    }
    else if (filter_index == 30)
    {
        pcolor = posterize(pcolor, 3.0);
    }
    else if (filter_index == 31)
    {
        pcolor = color_balance(pcolor, vec3(-1.00, -1.00, 1.00));
        pcolor = posterize(pcolor, 3.0);
        // nakljucnabarvadanatocka(4, 4);
    }
    else if (filter_index == 32)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.40, 0.16));
        pcolor = posterize(pcolor, 3.0);
    }
    else if (filter_index == 33)
    {
        pcolor = color_balance(pcolor, vec3(0.30, -0.40, 0.16));
        pcolor = posterize(pcolor, 3.0);
        // nakljucniefekt(rr, gg, bb);
    }

    gl_FragColor = vec4(pcolor, color.a);
}