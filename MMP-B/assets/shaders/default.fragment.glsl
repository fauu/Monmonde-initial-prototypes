#ifdef GL_ES
  #define LOWP lowp
  precision mediump float;
#else
  #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform vec4 u_fadeColor;
uniform float u_fadeLevel;

void main() {
  vec4 color = v_color * texture2D(u_texture, v_texCoords);
  vec4 fadedColor = color + ((u_fadeColor - color) * u_fadeLevel);
  gl_FragColor = vec4(fadedColor.r, fadedColor.g, fadedColor.b, color.a);
}