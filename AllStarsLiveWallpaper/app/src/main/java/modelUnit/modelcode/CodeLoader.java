package modelUnit.modelcode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import android.util.Log;


public class CodeLoader {
	//public static final String TAG = "EarthLiveWallpaper";
    
	private InputStream _inputStream;

    private float _vertices[];
    private float _textureCoords[];
    private float _normals[];

    
    public CodeLoader(InputStream inputStream) {
        
    	_inputStream = inputStream;
    
    }

    
    public float[] getVerticesCoords() {
        return _vertices;
    
    }

    
    public float[] getTextureCoords() {
        return _textureCoords;
    
    }

    
    public float[] getNormalsCoords() {
        return _normals;
    
    }

    
    public void load() throws IOException {
        
    	LineNumberReader input = new LineNumberReader(new InputStreamReader(_inputStream));
        String line = null;

        ArrayList<Float>   vertices      = new ArrayList<Float> ();
        ArrayList<Float>   textureCoords = new ArrayList<Float> ();
        ArrayList<Float>   normals       = new ArrayList<Float> ();

        ArrayList<Integer> vIndeces       = new ArrayList<Integer> ();
        ArrayList<Integer> tIndeces       = new ArrayList<Integer> ();
        ArrayList<Integer> nIndeces       = new ArrayList<Integer> ();

        try {
            
        	for(line = input.readLine();line != null; line = input.readLine()) {
                
        		if(line.length() > 0) {
                    
        			if(line.startsWith("v ")) {
                        StringTokenizer tok = new StringTokenizer(line);
                        tok.nextToken();
                        vertices.add( Float.parseFloat(tok.nextToken()) );
                        vertices.add( Float.parseFloat(tok.nextToken()) );
                        vertices.add( Float.parseFloat(tok.nextToken()) );
                    } 
        			else if(line.startsWith("vt ")) {
                        StringTokenizer tok = new StringTokenizer(line);
                        tok.nextToken();
                        textureCoords.add( Float.parseFloat(tok.nextToken()) );
                        textureCoords.add( Float.parseFloat(tok.nextToken()) );
                    } 
        			else if(line.startsWith("f ")) {
                        int[] val;

                        StringTokenizer tok = new StringTokenizer(line);
                        tok.nextToken();
                        val = parseIntTriple(tok.nextToken());
                        vIndeces.add( val[0] );
                        
                        if(val.length > 1 && val[1] > -1)
                            tIndeces.add( val[1] );
                        
                        if(val.length > 2 && val[2] > -1)
                            nIndeces.add( val[2]);

                        val = parseIntTriple(tok.nextToken());
                        vIndeces.add( val[0] );
                        
                        if(val.length > 1 && val[1] > -1)
                            tIndeces.add( val[1] );
                        
                        if(val.length > 2 && val[2] > -1)
                            nIndeces.add( val[2]);

                        val = parseIntTriple(tok.nextToken());
                        vIndeces.add( val[0] );
                        
                        if(val.length > 1 && val[1] > -1)
                            tIndeces.add( val[1] );
                        
                        if(val.length > 2 && val[2] > -1)
                            nIndeces.add( val[2]);
                    } 
        			else if(line.startsWith("vn ")) {
                        StringTokenizer tok = new StringTokenizer(line);
                        tok.nextToken();
                        normals.add( Float.parseFloat(tok.nextToken()) );
                        normals.add( Float.parseFloat(tok.nextToken()) );
                        normals.add( Float.parseFloat(tok.nextToken()) );
                    
        			}
                
        		}
            
        	}

            _vertices = new float [vIndeces.size()*3];
            
            for(int i=0; i<vIndeces.size(); i++) {
                _vertices[i*3+0] = vertices.get( vIndeces.get(i)*3+0 );
                _vertices[i*3+1] = vertices.get( vIndeces.get(i)*3+1 );
                _vertices[i*3+2] = vertices.get( vIndeces.get(i)*3+2 );
            
            }

            _textureCoords = new float [tIndeces.size()*2];
            
            for(int i=0; i<tIndeces.size(); i++) {
                _textureCoords[i*2+0] = textureCoords.get( tIndeces.get(i)*2+0 );
                _textureCoords[i*2+1] = textureCoords.get( tIndeces.get(i)*2+1 );
            
            }

            _normals = new float [nIndeces.size()*3];
            
            for(int i=0; i<nIndeces.size(); i++) {
                _normals[i*3+0] = normals.get( nIndeces.get(i)*3+0 );
                _normals[i*3+1] = normals.get( nIndeces.get(i)*3+1 );
                _normals[i*3+2] = normals.get( nIndeces.get(i)*3+2 );
            
            }
        
        } 
        catch(Exception ex) {
            //Log.e(TAG, "Error parsing data:");
            //Log.e(TAG, input.getLineNumber()+" : "+line);
        
        }
    
    }

    
    protected static int parseInt(String val) throws Exception {
        
    	if(val.length() == 0) {
           return -1;
        
    	}
        return Integer.parseInt(val);
    
    }

    
    protected static int[] parseIntTriple(String face) throws Exception {
        
    	int ix = face.indexOf("/");
        
    	if(ix == -1)
            return new int[] {Integer.parseInt(face)-1};
        
    	else {
            int ix2 = face.indexOf("/", ix+1);
            
            if(ix2 == -1) {
                return new int[] {Integer.parseInt(face.substring(0,ix))-1, Integer.parseInt(face.substring(ix+1))-1};
            } 
            else {
                return new int[] {parseInt(face.substring(0,ix))-1, parseInt(face.substring(ix+1,ix2))-1, parseInt(face.substring(ix2+1))-1 };
            
            }
        
    	}
    
    }

}
