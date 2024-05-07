package com.cg.farmirang.farm.feature;

import java.util.ArrayList;
import java.util.List;

class recommend_gpt {
    public class CropPlanting {
        public static void main(String[] args) {
            char[][] field = {
                    {'R', 'E', 'R', 'E', 'R', 'E'},
                    {'R', 'E', 'R', 'E', 'R', 'E'},
                    {'R', 'E', 'R', 'E', 'R', 'E'}
            };

            // 작물의 크기
            int cropWidth = 3;
            int cropHeight = 2;

            // 작물을 배치할 DTO 배열 초기화
            char[][] dto = new char[field.length][field[0].length];
            for (int i = 0; i < dto.length; i++) {
                for (int j = 0; j < dto[i].length; j++) {
                    dto[i][j] = ' ';
                }
            }

            // 작물을 배치하는 메소드 호출
            plantCrops(field, dto, cropWidth, cropHeight);

            // 결과 출력
            System.out.println("배치된 작물:");
            printField(dto);
        }

        // 작물을 배치하는 메소드
        public static void plantCrops(char[][] field, char[][] dto, int cropWidth, int cropHeight) {
            int totalPlanted = 0; // 전체 심어진 작물 수

            // field 순회
            for (int i = 0; i <= field.length - cropHeight; i++) {
                for (int j = 0; j <= field[0].length - cropWidth; j++) {
                    if (isAvailable(field, i, j, cropWidth, cropHeight)) {
                        // 작물을 심을 수 있는 경우
                        for (int k = i; k < i + cropHeight; k++) {
                            for (int l = j; l < j + cropWidth; l++) {
                                dto[k][l] = 'R';
                                totalPlanted++;
                            }
                        }
                    }

                    // 모든 작물을 심은 경우 종료
                    if (totalPlanted == (cropWidth * cropHeight)) {
                        return;
                    }
                }
            }
        }

        // 작물을 심을 수 있는지 확인하는 메소드
        public static boolean isAvailable(char[][] field, int row, int col, int cropWidth, int cropHeight) {
            for (int i = row; i < row + cropHeight; i++) {
                for (int j = col; j < col + cropWidth; j++) {
                    if (field[i][j] != 'R') {
                        return false;
                    }
                }
            }
            return true;
        }

        // 배열 출력 메소드
        public static void printField(char[][] field) {
            for (char[] row : field) {
                for (char cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        }
    }

}