from PIL import Image
import numpy as np

def load_image(path):
    return Image.open(path)

def slice_tileset(tileset_img, tile_width, tile_height):
    tiles = []
    tileset_width, tileset_height = tileset_img.size
    index = 0
    for y in range(0, tileset_height, tile_height):
        for x in range(0, tileset_width, tile_width):
            tile = tileset_img.crop((x, y, x + tile_width - 1, y + tile_height - 1))
            index += 1
            tiles.append(tile)
    return tiles

def compare_tiles(tile1, tile2):
    arr1 = np.array(tile1.convert("RGB"))
    arr2 = np.array(tile2.convert("RGB"))
    return np.array_equal(arr1, arr2)

def process_map(map_img, tiles, tile_width, tile_height):
    map_width, map_height = map_img.size
    indices = []

    

    for y in range(0, map_height, tile_height):
        row = []
        for x in range(0, map_width, tile_width):
            map_tile = map_img.crop((x, y, x + tile_width - 1, y + tile_height - 1))
            # Find matching tile
            index = -1  # Default if no match
            for i, tile in enumerate(tiles):
                if compare_tiles(map_tile, tile):
                    index = i 
                    break
            row.append(index)
        indices.append(row)
    return indices

def save_to_txt(indices, filename):
    with open(filename, 'w') as f:
        for row in indices:
            f.write(' '.join(map(str, row)) + '\n')

if __name__ == "__main__":
    # Paths to images
    map_tiles_path = "tileset.png"
    name = "level10"
    map_path = name + ".png"

    # Set your tile size
    TILE_WIDTH = 16
    TILE_HEIGHT = 8

    # Load images
    tileset_img = load_image(map_tiles_path)
    map_img = load_image(map_path)

    # Slice tileset
    tiles = slice_tileset(tileset_img, TILE_WIDTH, TILE_HEIGHT)

    # Process map to get indices
    indices = process_map(map_img, tiles, TILE_WIDTH, TILE_HEIGHT)

    # Save to txt
    save_to_txt(indices, name + ".txt")

    # print("Tile indices saved to map_indices.txt")
